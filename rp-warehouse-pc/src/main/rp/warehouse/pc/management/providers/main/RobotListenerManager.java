package rp.warehouse.pc.management.providers.main;

import org.apache.log4j.Logger;
import rp.util.Rate;
import rp.warehouse.pc.data.robot.Robot;

import javax.swing.*;

public class RobotListenerManager implements Runnable {
    private static final Logger logger = Logger.getLogger(RobotListenerManager.class);
    private final JTextArea textArea;
    private final Robot robot;
    private final int ID;
    private final DefaultListModel<String> listModel;

    /**
     * Create a robot listener manager for a given robot that polls the given robot for changes in information.
     * Polling was more ideal than listening because the logic in Robot was long and confusing, making coding for a
     * listener more difficult
     * @param robot robot to get information from
     * @param textArea textArea to set relevant information
     * @param listModel list model containing list of tasks for all robots
     * @param position position in the list model reserved for this RobotListenerManager
     */
    public RobotListenerManager(Robot robot, JTextArea textArea, DefaultListModel<String> listModel, int position) {
        this.robot = robot;
        this.textArea = textArea;
        this.listModel = listModel;
        this.ID = position;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Polls the robot for information 2 times per second, setting the current task and other information
     */
    @Override
    public void run() {
        Rate r = new Rate(2);
        while (true) {
            textArea.setText(robot.toString());
            listModel.set(ID, robot.getName() + ": " + robot.getTask().toString());
            r.sleep();
        }
    }
}
