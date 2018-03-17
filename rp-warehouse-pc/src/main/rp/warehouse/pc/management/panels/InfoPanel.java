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

        int i = 0;
        for (Robot robot : robots) {
            listElements.put(i, robot);
            managers.add(new RobotListenerManager(robot, listModel, i++));
        }

        this.setLayout(new BorderLayout());
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

        this.add(taskPanel, BorderLayout.NORTH);

    }

    public void setCurrentSelected(Robot currentSelected) {
        this.currentSelected = currentSelected;
    }
}
