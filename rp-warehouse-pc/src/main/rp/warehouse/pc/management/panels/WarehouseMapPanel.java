package rp.warehouse.pc.management.panels;

import rp.config.MobileRobotConfiguration;
import rp.robotics.MobileRobot;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.management.providers.ItemPoseProvider;
import rp.warehouse.pc.management.providers.RobotPoseProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WarehouseMapPanel extends JPanel {


    public WarehouseMapPanel(List<Robot> robots) {
        WarehouseMapVisualisation mapVisualisation = new WarehouseMapVisualisation(Warehouse.build(), Warehouse.build(), 200f, robots);

        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(800, 580));

        for (Robot robot : robots) {
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.22f, 0.12f), new RobotPoseProvider(robot)));
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.07f, 0.07f), new ItemPoseProvider(robot)));
        }

        this.setBackground(Color.WHITE);
    }

}
