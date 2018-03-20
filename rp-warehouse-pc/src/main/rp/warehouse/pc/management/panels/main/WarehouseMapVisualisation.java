package rp.warehouse.pc.management.panels.main;

import org.apache.log4j.Logger;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.management.providers.main.RobotPoseProvider;
import rp.warehouse.pc.route.Route;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class WarehouseMapVisualisation extends GridMapVisualisation {
    private static final Logger logger = Logger.getLogger(WarehouseMapVisualisation.class);
    private final List<Map.Entry<Robot, RobotPoseProvider>> robotsPoses;

    public WarehouseMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, List<Map.Entry<Robot, RobotPoseProvider>> robots) {
        super(_gridMap, _lineMap, _scaleFactor);
        this.robotsPoses = robots;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        renderPaths(g2);
    }

    /**
     * Renders all planned routes for all robots
     *
     * @param g2 Graphics2D
     */
    private void renderPaths(Graphics2D g2) {
        g2.setPaint(Color.RED);
        g2.setStroke(new BasicStroke(3));

        for (Map.Entry<Robot, RobotPoseProvider> robotPose : robotsPoses) {
            Route route = robotPose.getKey().getRoute();

            RobotLocation currentLocation = robotPose.getKey().getLocation();

            // Draw from pose to current location
            renderLine(robotPose.getValue().getPose().getLocation()
                    , currentLocation.toPose().getLocation()
                    , g2);

            if (route == null || route.isEmpty()) {
                logger.trace("Ignoring empty route");
            } else {

                // Draw future routes, if any
                for (int i : route) {
                    RobotLocation nextLocation = new RobotLocation(currentLocation);
                    changeLocation(nextLocation, nextLocation.getDirection());

                    renderLine(currentLocation.toGridPoint()
                            , nextLocation.toGridPoint()
                            , g2);

                    currentLocation = new RobotLocation(nextLocation);
                }
            }

        }

    }

    /**
     * Takes a location and changes it based on a direction
     *
     * @param location  Location to edit
     * @param direction Direction to move location by
     */
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
