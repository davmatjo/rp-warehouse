package rp.warehouse.pc.management.panels;

import org.jfree.ui.tabbedui.VerticalLayout;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.management.providers.RobotListenerManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class InfoPanel extends JPanel {

    public InfoPanel(List<Robot> robots) {
        List<RobotListenerManager> managers = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        int i = 0;
        for (Robot robot : robots) {
            managers.add(new RobotListenerManager(robot, listModel, i++));
            listModel.addElement("ERROR NO TASK");
        }

        this.setLayout(new VerticalLayout());
        JLabel tasks = new JLabel("Tasks");

        JList<String> list = new JList<>(listModel);
        this.add(tasks);
        this.add(list);

    }
}
