package rp.warehouse.pc.localisation.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import lejos.geom.Point;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.LocalisationCollection;
import rp.warehouse.pc.localisation.NoIdeaException;
import rp.warehouse.pc.localisation.Ranges;
import rp.warehouse.pc.localisation.interfaces.Localisation;

/**
 * An implementation of the localisation interface. Used to actually calculate
 * the location.
 *
 * @author Kieran
 */
public class Localiser implements Localisation {

	private static final Logger logger = Logger.getLogger(Localiser.class);
	private static final byte FORWARD = Protocol.NORTH, RIGHT = Protocol.EAST, BACKWARD = Protocol.SOUTH,
			LEFT = Protocol.WEST;
	private static final byte[] directionProtocol = new byte[] { FORWARD, RIGHT, BACKWARD, LEFT };
	private final LocalisationCollection northAssumption = new LocalisationCollection(Ranges.UP),
			eastAssumption = new LocalisationCollection(Ranges.RIGHT),
			southAssumption = new LocalisationCollection(Ranges.DOWN),
			westAssumption = new LocalisationCollection(Ranges.LEFT);
	private final Point[] directionPoint = new Point[4];
	private final byte MAX_RUNS = 100;
	private byte runCounter = 0;
	private final Random random = new Random();
	private byte previousDirection = 0;
	private final Communication comms;
	private Point relativePoint = new Point(0, 0);
	private final HashSet<Point> relativeVisitedPoints = new HashSet<Point>();

	/**
	 * An implementation of the Localisation interface.
	 */
	public Localiser(Communication comms) {
		relativeVisitedPoints.add(new Point(0, 0));
		directionPoint[Ranges.UP] = new Point(0, 1);
		directionPoint[Ranges.RIGHT] = new Point(1, 0);
		directionPoint[Ranges.DOWN] = new Point(0, -1);
		directionPoint[Ranges.LEFT] = new Point(-1, 0);
		this.comms = comms;
	}

	@Override
	public RobotLocation getPosition() throws NoIdeaException {
		// Get the readings from the sensors
		Ranges ranges = comms.getRanges();

		// Update ranges
		northAssumption.start(ranges);
		eastAssumption.start(ranges);
		southAssumption.start(ranges);
		westAssumption.start(ranges);

		// Run whilst there are multiple points, or the maximum iterations has occurred.
		while (northAssumption.notComplete() && eastAssumption.notComplete() && southAssumption.notComplete()
				&& westAssumption.notComplete() && runCounter++ < MAX_RUNS) {
			List<Byte> directions = ranges.getAvailableDirections();
			if (runCounter > 1) {
				directions.remove(directions.indexOf(Ranges.getOpposite(previousDirection)));
			}
			// Filter if it can go somewhere
			if (directions.size() > 0) {
				List<Byte> tempDirections = new ArrayList<>(directions);
				// Remove all directions that would lead to visiting the same point again.
				tempDirections.removeIf(d -> relativeVisitedPoints.contains(relativePoint.add(directionPoint[d])));
				if (tempDirections.size() > 0) {
					directions = tempDirections;
				}
			}
			logger.info("Available directions: " + directions);
			// Choose a random direction from the list of available directions.
			final byte direction = directions.get(random.nextInt(directions.size()));
			previousDirection = direction;
			final Point move = directionPoint[direction];
			logger.info("Chosen move: " + move);

			// Move the robot
			comms.sendMovement(directionProtocol[direction]);

			// Update relative position
			relativePoint = relativePoint.add(move);
			relativeVisitedPoints.add(relativePoint);
			logger.info("Previous direction: " + previousDirection);
			logger.info("Reversal rotation amount: " + direction);
			// Update ranges
			ranges = comms.getRanges();
			logger.info("Received ranges: " + ranges);
			// Rotate ranges
			ranges = Ranges.rotate(ranges, direction);
			logger.info("Rotated ranges: " + ranges);

			northAssumption.update(direction, ranges);
			eastAssumption.update(direction, ranges);
			southAssumption.update(direction, ranges);
			westAssumption.update(direction, ranges);
		}

		// One of the assumptions is complete, return the completed position
		if (northAssumption.isComplete()) {
			return new RobotLocation(northAssumption.getPoint(), directionProtocol[northAssumption.getHeading()]);
		} else if (eastAssumption.isComplete()) {
			return new RobotLocation(eastAssumption.getPoint(), directionProtocol[eastAssumption.getHeading()]);
		} else if (southAssumption.isComplete()) {
			return new RobotLocation(southAssumption.getPoint(), directionProtocol[southAssumption.getHeading()]);
		} else {
			return new RobotLocation(westAssumption.getPoint(), directionProtocol[westAssumption.getHeading()]);
		}
	}

}
