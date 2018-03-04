package rp.warehouse.nxt.communication;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import rp.util.HashMap;
import rp.warehouse.nxt.motion.Movement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication implements Runnable {
    private static final HashMap<Integer, Movement.Direction> commandTranslate = new HashMap<>();
    private final DataInputStream fromPC;
    private final DataOutputStream toPC;
    private final Movement robotMovement;
    private boolean open = true;
    // private final RobotInterface robotInterface;

    public Communication(Movement movement) {
        fillMap();

        System.out.println("Waiting on bluetooth");
        BTConnection connection = Bluetooth.waitForConnection();
        System.out.println("Connected");

        fromPC = connection.openDataInputStream();
        toPC = connection.openDataOutputStream();

        robotMovement = movement;
        // robotInterface = new RobotInterface(this);

        new Thread(this);
    }

    /**
     * A map to translate Protocol Integers to directions
     */
    private void fillMap() {
        commandTranslate.put(Protocol.NORTH, Movement.Direction.NORTH);
        commandTranslate.put(Protocol.EAST, Movement.Direction.EAST);
        commandTranslate.put(Protocol.SOUTH, Movement.Direction.SOUTH);
        commandTranslate.put(Protocol.WEST, Movement.Direction.WEST);
    }

    /**
     * Runs the receiveCommand method, then cleans up when finished
     */
    @Override
    public void run() {
        try {
            receiveCommand();
            fromPC.close();
            toPC.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives commands from the PC (currently only movement commands)
     *
     * @throws IOException - Any sort of communication error
     */
    private void receiveCommand() throws IOException {
        while (open) {
            Integer command = fromPC.readInt();
            if (command >= Protocol.NORTH && command <= Protocol.WEST) {
                robotMovement.move(commandTranslate.get(command));
            } else if (command == Protocol.PICKUP) {
                int countToPickup = fromPC.readInt();
                //if (robotInterface.pickup(countToPickup) {
                sendCommand(Protocol.PICKUP);
                sendCommand(Protocol.OK);
                //else {
                sendCommand(Protocol.PICKUP);
                sendCommand(Protocol.FAIL);
                //}
            }
        }
    }

    /**
     * Sends a Protocol command to the PC
     *
     * @param command int defined in protocol. Must be >= CANCEL
     */
    public void sendCommand(int command) {
        assert !(command <= Protocol.WEST && command >= Protocol.NORTH);

        try {
            toPC.write(command);
            toPC.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Ends and cleans up streams
     */
    public void close() {
        this.open = false;
    }
}
