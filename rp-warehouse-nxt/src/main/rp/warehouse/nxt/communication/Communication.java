package rp.warehouse.nxt.communication;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import rp.warehouse.nxt.motion.Movement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication implements Runnable {
    private final DataInputStream fromPC;
    private final DataOutputStream toPC;
    private final Movement robotMovement;
    // private final RobotInterface robotInterface;

    public Communication(Movement movement) {
        System.out.println("Waiting on bluetooth");
        BTConnection connection = Bluetooth.waitForConnection();
        System.out.println("Connected");

        fromPC = connection.openDataInputStream();
        toPC = connection.openDataOutputStream();

        robotMovement = movement;
        // robotInterface = new RobotInterface(this);

        new Thread(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int command = fromPC.readInt();
                switch (command) {
                    case Protocol.NORTH:
                        robotMovement.move(Movement.Direction.NORTH);
                        break;
                    case Protocol.EAST:
                        robotMovement.move(Movement.Direction.EAST);
                        break;
                    case Protocol.SOUTH:
                        robotMovement.move(Movement.Direction.SOUTH);
                        break;
                    case Protocol.WEST:
                        robotMovement.move(Movement.Direction.WEST);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(int command) {
        try {
            toPC.write(command);
            toPC.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
