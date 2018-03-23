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

/**
 * Contains a map that visualises the robots
 * @see WarehouseMapVisualisation
 */
public class WarehouseMapPanel extends JPanel {

    /**
     * Creates a JPanel that contains a map that visualises the state of given robots
     * @param robots robots to visualise
     */
    public WarehouseMapPanel(List<Robot> robots) {
        List<Map.Entry<Robot, RobotPoseProvider>> robotsPoses = new ArrayList<>();
        for (Robot robot : robots) {
            robotsPoses.add(new AbstractMap.SimpleEntry<>(robot, new RobotPoseProvider(robot)));
        }

        WarehouseMapVisualisation mapVisualisation = new WarehouseMapVisualisation(Warehouse.build(), Warehouse.build(), 200f, robotsPoses);

        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(800, 580));

        // Items in this simulation are added as if they were robots with a different width and height, as there is
        // always one item per robot, we can just use a pose provider
        for (Robot robot : robots) {
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.22f, 0.12f), new RobotPoseProvider(robot)));
            mapVisualisation.addRobot(new MobileRobot(new MobileRobotConfiguration(0.07f, 0.07f), new ItemPoseProvider(robot)));
        }

        this.setBackground(Color.WHITE);
    }

}
