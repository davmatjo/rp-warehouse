package rp.warehouse.pc.management.panels;

import org.jfree.ui.tabbedui.VerticalLayout;

import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.management.providers.RobotListenerManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoPanel extends JPanel {

    public InfoPanel(List<Robot> robots) {
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(new Dimension(500, 600));

        List<RobotListenerManager> managers = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.setSize(robots.size());

        Map<Integer, Robot> listElements = new HashMap<>();

        JPanel robotPanel = new JPanel();
        robotPanel.setLayout(new GridLayout(3, 1, 0, 10));

        int i = 0;
        for (Robot robot : robots) {
            listElements.put(i, robot);
            JTextArea robotInfo = new JTextArea();
            managers.add(new RobotListenerManager(robot, robotInfo, listModel, i++));

            JPanel panel = new JPanel();
            panel.setBackground(Color.RED);
            panel.setLayout(new VerticalLayout());
            panel.add(new JLabel(robot.getName()));
            panel.add(robotInfo);
            robotPanel.add(panel);

        }

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout(0, 5));
        JLabel tasks = new JLabel("Tasks");

        JList<String> list = new JList<>(listModel);
        taskPanel.add(tasks, BorderLayout.NORTH);
        taskPanel.add(list, BorderLayout.CENTER);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((e) -> {
            int[] selections = list.getSelectedIndices();
            if (selections.length != 0) {
                for (int selection : selections) {
                    listElements.get(selection).cancelJob();
                }
            }
        });
        taskPanel.add(cancel, BorderLayout.SOUTH);
        taskPanel.setBackground(Color.ORANGE);


        JPanel logo = new JPanel();
        logo.setSize(150, 100);
        ImageIcon icon = new ImageIcon("./logo.jpg");
        JLabel label = new JLabel();
        label.setSize(150, 100);
        label.setIcon(icon);
        logo.add(label);

        this.add(taskPanel, BorderLayout.NORTH);
        this.add(robotPanel, BorderLayout.CENTER);
        this.add(logo, BorderLayout.SOUTH);

    }

}
