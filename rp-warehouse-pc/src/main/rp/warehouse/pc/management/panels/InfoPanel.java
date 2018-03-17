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
    private Robot currentSelected;

    public InfoPanel(List<Robot> robots) {
        List<RobotListenerManager> managers = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.setSize(robots.size());

        Map<Integer, Robot> listElements = new HashMap<>();

        JPanel robotPanel = new JPanel();
        robotPanel.setLayout(new GridLayout(1, 3));

        int i = 0;
        for (Robot robot : robots) {
            listElements.put(i, robot);
            JTextArea robotInfo = new JTextArea();
            robotInfo.setMargin(new Insets(0, 5, 5, 5));
            managers.add(new RobotListenerManager(robot, robotInfo, listModel, i++));

            JPanel panel = new JPanel();
            panel.setBackground(Color.RED);
            panel.setLayout(new VerticalLayout());
            panel.add(new JLabel(robot.getName()));
            panel.add(robotInfo);
            robotPanel.add(panel);

        }

        this.setLayout(new VerticalLayout());
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new VerticalLayout());
        JLabel tasks = new JLabel("Tasks");

        JList<String> list = new JList<>(listModel);
        taskPanel.add(tasks);
        taskPanel.add(list);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((e) -> {
            int[] selections = list.getSelectedIndices();
            if (selections.length != 0) {
                for (int selection : selections) {
                    listElements.get(selection).cancelJob();
                }
            }
        });
        taskPanel.add(cancel);
        taskPanel.setBackground(Color.ORANGE);

        taskPanel.add(robotPanel);

        JPanel logo = new JPanel();
        logo.setSize(300, 200);
        ImageIcon icon = new ImageIcon("./logo.jpg");
        JLabel label = new JLabel();
        label.setIcon(icon);
        logo.add(label);
        taskPanel.add(logo);

        this.add(taskPanel);

    }

    public void setCurrentSelected(Robot currentSelected) {
        this.currentSelected = currentSelected;
    }
}
