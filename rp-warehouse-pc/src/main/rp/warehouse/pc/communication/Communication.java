package rp.warehouse.pc.communication;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import rp.warehouse.pc.data.Robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Communication implements Runnable {
    private final String ID;
    private final String name;
    private final Robot robot;
    private DataOutputStream toNXT;

    public enum Protocol{
        FAIL, OK
    }

    public Communication(String ID, String name, Robot robot) {
        this.ID = ID;
        this.name = name;
        this.robot = robot;
        new Thread(this);
    }

    /**
     * This thread will loop indefinitely, attempting to receive data from the robot
     */
    @Override
    public void run() {
        try {

            NXTComm comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
            NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, ID);
            comm.open(nxt);

            DataInputStream fromNXT = new DataInputStream(comm.getInputStream());
            toNXT = new DataOutputStream(comm.getOutputStream());

            // Read data, run appropriate methods
            while (true) {
                int input = fromNXT.readInt();
                if (input <= 1) {
                    robot.setResponse(input == 0 ? Robot.Response.OK : Robot.Response.FAIL);
                } else if (input == 2){
                    robot.cancelJob();
                }
            }
        } catch (NXTCommException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(int data) {
        try {
            toNXT.write(data);
            toNXT.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
