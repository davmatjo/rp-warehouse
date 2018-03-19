package rp.warehouse.pc.management.panels;

import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.implementation.Localiser;

import java.awt.*;
import java.util.stream.Stream;

public class LocaliserMapVisualisation extends GridMapVisualisation {
    private Localiser localiser;
    private Color[] directionColours = new Color[] {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA};

    public LocaliserMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, Localiser localiser) {
        super(_gridMap, _lineMap, _scaleFactor);

        this.localiser = localiser;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        int i = 0;

        g2.setPaint(Color.RED);
        g2.setStroke(new BasicStroke(3));
        renderLine(new RobotLocation(0, 1, 3).toGridPoint(), new RobotLocation(0, 1, 3).toGridPoint(), g2);

        for (Stream<RobotLocation> points : localiser.getCurrentLocations()) {

            g2.setPaint(directionColours[i]);
            g2.setStroke(new BasicStroke(3));
            points.forEach((a) ->  {
                renderLine(a.toGridPoint(), a.toGridPoint(), g2);
                System.out.println(a);
            });

            i++;
        }

    }
}
