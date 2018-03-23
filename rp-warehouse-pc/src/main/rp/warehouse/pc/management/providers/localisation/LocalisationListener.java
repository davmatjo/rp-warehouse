package rp.warehouse.pc.management.providers.localisation;

import rp.warehouse.pc.data.robot.utils.RobotLocation;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author dxj786
 */
public interface LocalisationListener {

    /**
     * Notify any listeners of new possible points for localisation
     * @param points possible points
     */
    void newPoints(List<Stream<RobotLocation>> points);
}
