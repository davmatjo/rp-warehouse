package rp.warehouse.pc.management.providers;

import org.apache.log4j.Logger;
import rp.util.Rate;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;

import javax.swing.*;

public class RobotListenerManager implements Runnable {
    private static final Logger logger = Logger.getLogger(RobotListenerManager.class);
    private final Robot robot;
    private final int ID;
    private final DefaultListModel<String> listModel;

    public RobotListenerManager(Robot robot, DefaultListModel<String> listModel, int position) {
        this.robot = robot;
        this.listModel = listModel;
        this.ID = position;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run() {
        Rate r = new Rate(2);
        Task previousTask = robot.getTask();
        while (true) {
            listModel.set(ID, robot.getTask().toString());
            r.sleep();
        }
    }
}
