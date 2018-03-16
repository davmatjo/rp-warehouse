package rp.warehouse.pc.management.providers;

import rp.util.Rate;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;

import javax.swing.*;

public class RobotListenerManager implements Runnable {
    private final Robot robot;
    private final int ID;
    private final DefaultListModel<String> listModel;

    public RobotListenerManager(Robot robot, DefaultListModel<String> listModel, int position) {
        this.robot = robot;
        this.listModel = listModel;
        this.ID = position;
        new Thread(this).setDaemon(true);
    }

    @Override
    public void run() {
        Rate r = new Rate(16);
        Task previousTask = robot.getTask();
        while (true) {
            if (!previousTask.equals(robot.getTask())) {
                listModel.set(ID, robot.getTask().toString());
            }
            r.sleep();
        }
    }
}
