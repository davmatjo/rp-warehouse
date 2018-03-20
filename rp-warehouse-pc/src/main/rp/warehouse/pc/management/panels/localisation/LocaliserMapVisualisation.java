package rp.warehouse.pc.management.panels.localisation;

import lejos.geom.Line;
import lejos.robotics.navigation.Pose;
import rp.geom.GeometryUtils;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.providers.localisation.LocalisationListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LocaliserMapVisualisation extends GridMapVisualisation implements LocalisationListener {
    private Localiser localiser;
    private Color[] directionColours = new Color[] {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA};
    private List<Stream<RobotLocation>> possiblePoints = new ArrayList<>();

    public LocaliserMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, Localiser localiser) {
        super(_gridMap, _lineMap, _scaleFactor);

        this.localiser = localiser;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        renderPossiblePoints(g2);
    }

    private void renderPossiblePoints(Graphics2D g2) {
        int i = 0;

        for (Stream<RobotLocation> points : localiser.getCurrentLocations()) {

            g2.setPaint(directionColours[i]);
            g2.setStroke(new BasicStroke(3));

            points.forEach((location) -> drawQuadrilateral(0.1f, 0.1f, location.toPose(), g2));

            i++;
        }
    }

    /**
     * **adapted from rp-pc** gets a quadrilateral using a width and height then draws it
     * @param width width of quad
     * @param height height of quad
     * @param pose position of centre of quad
     * @param g2 graphics2D
     */
    private void drawQuadrilateral(float width, float height, Pose pose, Graphics2D g2) {

        width = width / 2f;
        height = height / 2f;

        renderRelative(new Line[] {
                // front
                new Line(width, height, width, -height),
                // back
                new Line(-width, height, -width, -height),
                // top
                new Line(-width, height, width, height),
                // bottom
                new Line(-width, -height, width, -height),

        }, pose, g2);
    }

    /**
     * **From rp-pc** renders a line array around a pose
     * @param _lines Lines to draw
     * @param _pose Centre position
     * @param _g2 Graphics2D
     */
    private void renderRelative(Line[] _lines, Pose _pose, Graphics2D _g2) {

        for (Line l : _lines) {

            l = GeometryUtils.transform(_pose, l);
            _g2.drawLine((int) scale(l.x1) + X_MARGIN, (int) scale(flipY(l.y1))
                            + X_MARGIN, (int) scale(l.x2) + X_MARGIN,
                    (int) scale(flipY(l.y2)) + X_MARGIN);
        }

    }

    @Override
    public void newPoints(List<Stream<RobotLocation>> points) {
        this.possiblePoints = points;
    }
}
