package rp.warehouse.pc.management.providers.localisation;

import rp.warehouse.pc.data.robot.utils.RobotLocation;

import java.util.List;
import java.util.stream.Stream;

public interface LocalisationListener {

    void newPoints(List<Stream<RobotLocation>> points);
}
