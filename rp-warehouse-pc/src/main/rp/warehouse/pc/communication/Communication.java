package rp.warehouse.pc.communication;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import rp.warehouse.pc.data.Robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Communication implements Runnable {
    private final Robot robot;
    private final DataInputStream fromNXT;
    private final DataOutputStream toNXT;
    private CountDownLatch waitForMovement = new CountDownLatch(1);
    private boolean open = true;

    public Communication(String ID, String name, Robot robot) {
        this.robot = robot;

        NXTComm nxtComm = null;
        try {

            nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
            NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, ID);
            nxtComm.open(nxt);

        } catch (NXTCommException e) {
            System.err.println("Unable to open NXT Connection: " + e.getMessage());
        }

        assert (nxtComm != null);
        fromNXT = new DataInputStream(nxtComm.getInputStream());
        toNXT = new DataOutputStream(nxtComm.getOutputStream());

        new Thread(this);
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
            int input = fromNXT.readInt();
            switch (input) {

                // Feedback from movement
                case Protocol.OK:
                case Protocol.FAIL: {
                    waitForMovement.countDown();
                    waitForMovement = new CountDownLatch(1);
                    break;
                }

                // Commands from RobotInterface
                case Protocol.CANCEL: {
                    robot.cancelJob();
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
            toNXT.write(data);
            toNXT.flush();
        } catch (IOException e) {
            System.err.println("Bluetooth IO Error in send: " + e.getMessage());
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
        assert direction <= Protocol.SOUTH;

        sendData(direction);

        try {
            waitForMovement.await();
            waitForMovement = new CountDownLatch(1);
        } catch (InterruptedException e) {
            System.err.println("Interrupted somehow: " + e.getMessage());
        }
    }

    /**
     * Close the connection and clean up
     */
    public void close() {
        open = false;
    }
}
