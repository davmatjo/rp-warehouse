package rp.warehouse.pc.management.panels;

import org.apache.log4j.Logger;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.RobotLocation;
import rp.warehouse.pc.route.Route;

import java.awt.*;
import java.util.List;

public class WarehouseMapVisualisation extends GridMapVisualisation {
    private static final Logger logger = Logger.getLogger(WarehouseMapVisualisation.class);
    private final List<Robot> robots;

    public WarehouseMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, List<Robot> robots) {
        super(_gridMap, _lineMap, _scaleFactor);
        this.robots = robots;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        renderPaths(g2);
    }

    private void renderPaths(Graphics2D g2) {
        g2.setPaint(Color.RED);
        g2.setStroke(new BasicStroke(3));

        for (Robot robot : robots) {
            try {
                Route route = robot.getRoute();
                RobotLocation currentLocation = robot.getLocation();

                for (int i : route) {
                    RobotLocation nextLocation = new RobotLocation(currentLocation);
                    changeLocation(nextLocation, i);

                    renderLine(robot.getLocation().toPose().getLocation()
                            ,  nextLocation.toPose().getLocation()
                            ,  g2);

                    currentLocation = new RobotLocation(nextLocation);
                }
            } catch (IndexOutOfBoundsException e) {
                logger.debug("Ignoring non existent path");
            }
        }

    }

    private void changeLocation(RobotLocation location, int direction) {
        switch (direction) {
            case Protocol.NORTH:
                location.setY(location.getY() + 1);
                break;
            case Protocol.EAST:
                location.setX(location.getX() + 1);
                break;
            case Protocol.SOUTH:
                location.setY(location.getY() - 1);
                break;
            case Protocol.WEST:
                location.setX(location.getX() - 1);
        }
    }

}
