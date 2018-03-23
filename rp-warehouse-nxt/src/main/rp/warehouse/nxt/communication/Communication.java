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

        BTConnection connection = Bluetooth.waitForConnection();

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
            // Read command from pc
            Integer command = fromPC.readInt();

            // Movement command
            if (command >= Protocol.NORTH && command <= Protocol.WEST) {
                robotMovement.move(commandTranslate.get(command));
                sendCommand(1);

            // Pickup and dropoff command
            } else if (command == Protocol.PICKUP) {
                robotInterface.pickup(fromPC.readInt());

            // Localisation
            } else if (command == Protocol.LOCALISE) {
                float[] ranges = rangeFind.getRanges();
                sendCommand(Protocol.LOCALISE);
                for (float range : ranges) {
                    System.out.println(range);
                    sendFloat(range);
                }

            // Set direction facing once localised
            } else if (command == Protocol.SETDIR) {
                robotMovement.setDirection(fromPC.readInt());
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

    /**
     * Sends a float for localisation
     * @param data float to send
     */
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
