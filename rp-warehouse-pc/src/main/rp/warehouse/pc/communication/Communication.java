package rp.warehouse.pc.communication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.localisation.Ranges;

public class Communication implements Runnable {
    private static final Logger logger = Logger.getLogger(Communication.class);
    private final String name;
    private final Robot robot;
    private final DataInputStream fromNXT;
    private final DataOutputStream toNXT;
    private final Object waitForMovement = new Object();
    private final Object waitForPickup = new Object();
    private final Object waitForRanges = new Object();
    private volatile int pickupCount = 0;
    private final float[] ranges = new float[4];
    private boolean open = true;

    public Communication(String ID, String name, Robot robot) throws IOException {
        this.robot = robot;
        this.name = name;

        NXTComm nxtComm;
        try {
            logger.trace(name + ": Creating factory");
            nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
            logger.trace(name + ": Creating NXTInfo");
            NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, ID);
            logger.trace(name + ": Opening NXTComm");
            nxtComm.open(nxt);

        } catch (NXTCommException e) {
            logger.error("Unable to open NXT Connection: " + e.getMessage());
            throw new IOException(e);
        }

        fromNXT = new DataInputStream(nxtComm.getInputStream());
        toNXT = new DataOutputStream(nxtComm.getOutputStream());
    }

    /**
     * Runs the receiveData method, then cleans up once finished
     */
    @Override
    public void run() {
        try {
            receiveData();

            // When finished, flush and close
            fromNXT.close();
            toNXT.flush();
            toNXT.close();

        } catch (IOException e) {
            System.err.println("Bluetooth IO Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loops indefinitely, reading data from the NXT. Calls appropriate methods according to protocol
     *
     * @throws IOException If something goes wrong with the stream
     */
    private void receiveData() throws IOException {
        while (open) {

            // Read input and act accordingly
            logger.trace(name + ": Waiting to receive");
            int input = fromNXT.readInt();
            logger.trace(name + ": Received " + input);
            switch (input) {

                // Feedback from movement
                case Protocol.OK:
                case Protocol.FAIL: {
                    synchronized (waitForMovement) {
                        waitForMovement.notifyAll();
                    }
                    break;
                }

                // Commands from RobotInterface
                case Protocol.CANCEL: {
                    robot.cancelJob();
                    break;
                }

                case Protocol.PICKUP: {
                    input = fromNXT.readInt();
                    logger.trace(name + ": Received " + input);
                    synchronized (waitForPickup) {
                        pickupCount = input;
                        waitForPickup.notifyAll();
                    }
                    break;
                }

                case Protocol.LOCALISE: {
                    for (int i=0; i < 4; i++) {
                        ranges[i] = fromNXT.readFloat();
                    }
                    synchronized (waitForRanges) {
                        ranges.notifyAll();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Send data to the robot according to the protocol
     *
     * @param data int: defined in communication.Protocol
     */
    private void sendData(int data) {
        try {
            logger.debug(name + ": Sending " + data);
            toNXT.writeInt(data);
            toNXT.flush();
            logger.trace(name + ": Sent " + data);
        } catch (IOException e) {
            logger.error("Bluetooth IO Error in send: " + e.getMessage());
            open = false;
        }
    }

    /**
     * Sends a movement command to the NXT and waits for the command to finish
     *
     * @param direction - Protocol.NORTH, EAST, SOUTH, or WEST
     */
    public void sendMovement(int direction) {
        assert direction >= Protocol.NORTH;
        assert direction <= Protocol.WEST;

        try {
            logger.trace(name + ": Sending direction: " + direction);
            synchronized (waitForMovement) {
                sendData(direction);
                logger.trace("Waiting");
                waitForMovement.wait();
                logger.trace("Finished waiting");
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted somehow: " + e.getMessage());
        }
    }

    /**
     * Send the NXT a signal to pickup a number of items equal to the count
     *
     * @return - true if the correct number of items was picked up
     */
    public int sendPickupRequest() {

        try {
            synchronized (waitForPickup) {
                sendData(Protocol.PICKUP);
                waitForPickup.wait();
                return pickupCount;
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted somehow: " + e.getMessage());
            return -1;
        }
    }

    public Ranges getRanges() {
        try {
            synchronized (waitForRanges) {
                sendData(Protocol.PICKUP);
                waitForRanges.wait();
                return Ranges.fromArray(ranges);
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted somehow: " + e.getMessage());
            return Ranges.fromArray(ranges);
        }
    }

    /**
     * Close the connection and clean up
     */
    public void close() {
        open = false;
    }
}
