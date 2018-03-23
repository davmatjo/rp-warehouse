package rp.warehouse.pc.management.panels.main;

import lejos.robotics.navigation.Pose;
import org.apache.log4j.Logger;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.management.providers.main.RobotPoseProvider;
import rp.warehouse.pc.route.Route;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * A map visualisation for given robots that displays robots, items, and information about each robot floating next to it
 * @author dxj786
 * @see GridMapVisualisation
 */
public class WarehouseMapVisualisation extends GridMapVisualisation {
    private static final Logger logger = Logger.getLogger(WarehouseMapVisualisation.class);
    private final List<Map.Entry<Robot, RobotPoseProvider>> robotsPoses;

    /**
     * Creates a map visualisation for some given robots
     * @param _gridMap gridMap
     * @param _lineMap lineMap
     * @param _scaleFactor scale factor
     * @param robots list of robots with their locations to visualise
     */
    WarehouseMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, List<Map.Entry<Robot, RobotPoseProvider>> robots) {
        super(_gridMap, _lineMap, _scaleFactor);
        this.robotsPoses = robots;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        renderPaths(g2);
        renderRelativeText(g2);
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
                    nextLocation.setDirection(i);
                    nextLocation.forward();

                    renderLine(currentLocation.toGridPoint()
                            , nextLocation.toGridPoint()
                            , g2);

                    currentLocation = new RobotLocation(nextLocation);
                }
            }

        }

    }

    /**
     * Renders strings for some robots that follow them around the map
     * @param g2 Graphics2D
     */
    private void renderRelativeText(Graphics2D g2) {
        g2.setPaint(new Color(13, 71, 161));
        g2.setStroke(new BasicStroke(3));

        for (Map.Entry<Robot, RobotPoseProvider> robotPose : robotsPoses) {

            Pose current = robotPose.getValue().getPose();

            float x = scale(current.getX()) + X_MARGIN + 15;
            float y = (float) scale(flipY(current.getY())) + Y_MARGIN - 30;
            g2.drawString(robotPose.getKey().getName(), x, y);

            g2.drawString(robotPose.getKey().getTask().count + " to pick up", x, y + 15);
        }
    }

}
