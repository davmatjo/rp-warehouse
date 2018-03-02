package rp.warehouse.nxt;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication implements Runnable {
    private final DataInputStream fromPC;
    private final DataOutputStream toPC;
    // private final Movement robotMovement;
    // private final RobotInterface robotInterface;

    public Communication() {
        System.out.println("Waiting on bluetooth");
        BTConnection connection = Bluetooth.waitForConnection();
        System.out.println("Connected");

        fromPC = connection.openDataInputStream();
        toPC = connection.openDataOutputStream();

        // robotMovement = new Movement();
        // robotInterface = new RobotInterface(this);

        Thread receive = new Thread(this);
        receive.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int command = fromPC.readInt();
                switch (command) {
                    case Protocol.NORTH:
                        //robotMovement.move(Movement.Direction.North);
                        break;
                    case Protocol.EAST:
                        //robotMovement.move(Movement.Direction.East);
                        break;
                    case Protocol.SOUTH:
                        //robotMovement.move(Movement.Direction.East);
                        break;
                    case Protocol.WEST:
                        //robotMovement.move(Movement.Direction.East);
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
