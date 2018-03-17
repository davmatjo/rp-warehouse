package rp.warehouse.pc.management.panels;

import rp.config.MobileRobotConfiguration;
import rp.robotics.MobileRobot;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.management.providers.ItemPoseProvider;
import rp.warehouse.pc.management.providers.RobotPoseProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WarehouseMapPanel extends JPanel {
    private static final GridMapVisualisation mapVisualisation = new GridMapVisualisation(Warehouse.build(), Warehouse.build(), 200f);


    public WarehouseMapPanel(List<Robot> robots) {
        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(1000, 600));

        for (Robot robot : robots) {
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.22f, 0.12f), new RobotPoseProvider(robot)));
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.07f, 0.07f), new ItemPoseProvider(robot)));
        }
    }
}
