package rp.warehouse.nxt.communication;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
//import rp.util.HashMap;
import rp.util.HashMap;
import rp.warehouse.nxt.interaction.RobotInterfaceController;
import rp.warehouse.nxt.localisation.Ranges;
import rp.warehouse.nxt.motion.MotionController;
import rp.warehouse.nxt.motion.Movement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication extends Thread {
    private static final HashMap<Integer, Movement.Direction> commandTranslate = new HashMap<>();
    private final DataInputStream fromPC;
    private final DataOutputStream toPC;
    private final MotionController robotMovement;
    private boolean open = true;
    private final RobotInterfaceController robotInterface;
    private final Ranges rangeFind;

    public Communication(MotionController movement) {
        fillMap();

        System.out.println("Waiting on bluetooth");
        BTConnection connection = Bluetooth.waitForConnection();
        System.out.println("Connected");

        fromPC = connection.openDataInputStream();
        toPC = connection.openDataOutputStream();

        robotMovement = movement;
        robotInterface = new RobotInterfaceController(this);

        rangeFind = new Ranges(robotMovement);
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
            System.out.println("New Thread");
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
                sendCommand(1);
            } else if (command == Protocol.PICKUP) {
                robotInterface.pickup(fromPC.readInt());
            } else if (command == Protocol.LOCALISE) {
                float[] ranges = rangeFind.getRanges();
                sendCommand(Protocol.LOCALISE);
                for (float range : ranges) {
                    System.out.println(range);
                    sendFloat(range);
                }
            }
        }
    }

    /**
     * Sends a Protocol command to the PC
     *
     * @param command int defined in protocol. Must be >= CANCEL
     */
    public void sendCommand(int command) {
        try {
            toPC.writeInt(command);
            toPC.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private void sendFloat(float data) {
        try {
            toPC.writeFloat(data);
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
