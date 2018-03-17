package rp.warehouse.pc.management.panels;

import org.jfree.ui.tabbedui.VerticalLayout;

import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.management.providers.RobotListenerManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InfoPanel extends JPanel {

    public InfoPanel(List<Robot> robots) {
        List<RobotListenerManager> managers = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.setSize(robots.size());

        int i = 0;
        for (Robot robot : robots) {
            listModel.addElement("ERROR NO TASK");
            managers.add(new RobotListenerManager(robot, listModel, i++));
        }

        this.setLayout(new BorderLayout());
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new VerticalLayout());
        JLabel tasks = new JLabel("Tasks");

        JList<String> list = new JList<>(listModel);
        taskPanel.add(tasks);
        taskPanel.add(list);

        this.add(taskPanel, BorderLayout.NORTH);

    }
}
