package rp.warehouse.pc.management.panels.main;

import org.jfree.ui.tabbedui.VerticalLayout;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RewardCounter;
import rp.warehouse.pc.management.providers.main.RobotListenerManager;
import rp.warehouse.pc.management.providers.main.WarehouseInfoListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoPanel extends JPanel implements WarehouseInfoListener {
    private JLabel warehouseInfo;
    private float totalReward = 0f;
    private int jobsCancelled = 0;
    private int jobsCompleted = 0;
    private int uncompleted = 0;

    public InfoPanel(List<Robot> robots) {
        // Panel properties
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(new Dimension(500, 600));

        // List model
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.setSize(robots.size());

        // Map a DefaultListModel position to a robot
        Map<Integer, Robot> listElements = new HashMap<>();

        JPanel robotPanel = new JPanel();
        robotPanel.setLayout(new GridLayout(3, 1, 0, 10));

        // Each robot has a list position, robot info panel and data listener
        int i = 0;
        for (Robot robot : robots) {
            listElements.put(i, robot);

            // JTextArea is updated with information by RobotListenerManager
            JTextArea robotInfo = new JTextArea();
            new RobotListenerManager(robot, robotInfo, listModel, i++);

            // Panel to hold robot name, and JTextArea made previously
            JPanel panel = new JPanel();
            panel.setBackground(Color.RED);
            panel.setLayout(new VerticalLayout());

            JLabel robotName = new JLabel(robot.getName());
            robotName.setHorizontalAlignment(JLabel.CENTER);

            // Add elements to panels
            panel.add(robotName);
            panel.add(robotInfo);
            robotPanel.add(panel);
        }

        // Set up tasks panel
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout(0, 5));
        JLabel tasks = new JLabel("Tasks");

        // Set up JList and add to tasks panel
        JList<String> list = new JList<>(listModel);
        taskPanel.add(tasks, BorderLayout.NORTH);
        taskPanel.add(list, BorderLayout.CENTER);

        // Cancel button uses map to get robot back from JList selection
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((e) -> {
            int[] selections = list.getSelectedIndices();
            if (selections.length != 0) {
                for (int selection : selections) {
                    listElements.get(selection).cancelJob();
                }
            }
        });

        // Add the cancel button to tasks panel and set the background
        taskPanel.add(cancel, BorderLayout.SOUTH);
        taskPanel.setBackground(Color.ORANGE);

        // Overall warehouse info label
        warehouseInfo = new JLabel("No info yet");
        warehouseInfo.setVerticalAlignment(JLabel.CENTER);
        warehouseInfo.setHorizontalAlignment(JLabel.CENTER);
        updateText();
        RewardCounter.addListener(this);

        // Logo
        JPanel bottom = new JPanel();

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(new ImageIcon("./boiExpress.png").getImage()));
        bottom.add(warehouseInfo);
        bottom.add(label);

        this.add(taskPanel, BorderLayout.NORTH);
        this.add(robotPanel, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);

    }

    @Override
    public void rewardChanged(float newReward) {
        totalReward = newReward;
        updateText();
    }

    @Override
    public void jobCountChanged(int newJobCount) {
        jobsCompleted = newJobCount;
        updateText();
    }

    @Override
    public void cancelledJobsChanged(int newCancelledCount) {
        jobsCancelled = newCancelledCount;
        updateText();
    }

    @Override
    public void uncompletedJobsChanged(int newUncompletedCount) {
        uncompleted = newUncompletedCount;
        updateText();
    }

    private void updateText() {
        warehouseInfo.setText("Total reward: " + totalReward + " from " + jobsCompleted + " jobs. " + jobsCancelled + " cancelled " + uncompleted + " uncompleted");
    }
}
