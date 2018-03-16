package rp.warehouse.pc.management;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
import org.jfree.ui.tabbedui.VerticalLayout;
import rp.config.MobileRobotConfiguration;
import rp.robotics.MobileRobot;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.Warehouse;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class MainView extends JFrame {
    private static final GridMapVisualisation mapVisualisation = new GridMapVisualisation(Warehouse.build(), Warehouse.build(), 200f);


    public MainView(List<Robot> robots) {
        super();
        JPanel top = new JPanel();
        top.setMinimumSize(new Dimension(900, 600));

        mapVisualisation.setPreferredSize(new Dimension(1000, 600));

        for (Robot robot : robots) {
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.12f, 0.22f), new RobotPoseProvider(robot)));
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.07f, 0.07f), new ItemPoseProvider(robot)));
        }

        JPanel info = new JPanel();
        info.setLayout(new VerticalLayout());
        JLabel tasks = new JLabel("Tasks");
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Robot robot : robots) {
            listModel.addElement(robot.getTask().toString());
        }

        JList<String> list = new JList<>(listModel);
        info.add(tasks);
        info.add(list);

        top.setLayout(new FlowLayout());
        top.add(mapVisualisation);
        top.add(info);

        this.add(top);
        this.pack();
        this.setSize(top.getPreferredSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
