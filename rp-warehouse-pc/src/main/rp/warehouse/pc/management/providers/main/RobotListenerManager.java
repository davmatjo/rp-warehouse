package rp.warehouse.pc.management.providers.main;

import org.apache.log4j.Logger;
import rp.util.Rate;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.Robot;

import javax.swing.*;

public class RobotListenerManager implements Runnable {
    private static final Logger logger = Logger.getLogger(RobotListenerManager.class);
    private final JTextArea textArea;
    private final Robot robot;
    private final int ID;
    private final DefaultListModel<String> listModel;

    public RobotListenerManager(Robot robot, JTextArea textArea, DefaultListModel<String> listModel, int position) {
        this.robot = robot;
        this.textArea = textArea;
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
            textArea.setText(robot.toString());
            listModel.set(ID, robot.getName() + ": " + robot.getTask().toString());
            r.sleep();
        }
    }
}
