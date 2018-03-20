package rp.warehouse.pc.management.panels.main;

import rp.config.MobileRobotConfiguration;
import rp.robotics.MobileRobot;
import rp.util.AbstractMap;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.management.providers.main.ItemPoseProvider;
import rp.warehouse.pc.management.providers.main.RobotPoseProvider;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarehouseMapPanel extends JPanel {


    public WarehouseMapPanel(List<Robot> robots) {
        List<Map.Entry<Robot, RobotPoseProvider>> robotsPoses = new ArrayList<>();
        for (Robot robot : robots) {
            robotsPoses.add(new AbstractMap.SimpleEntry<>(robot, new RobotPoseProvider(robot)));
        }

        WarehouseMapVisualisation mapVisualisation = new WarehouseMapVisualisation(Warehouse.build(), Warehouse.build(), 200f, robotsPoses);

        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(800, 580));

        for (Robot robot : robots) {
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.22f, 0.12f), new RobotPoseProvider(robot)));
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.07f, 0.07f), new ItemPoseProvider(robot)));
        }

        this.setBackground(Color.WHITE);
    }

}
