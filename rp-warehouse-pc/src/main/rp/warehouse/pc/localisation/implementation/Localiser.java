package rp.warehouse.pc.localisation.implementation;

import java.util.*;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import lejos.geom.Point;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.LocalisationCollection;
import rp.warehouse.pc.localisation.NoIdeaException;
import rp.warehouse.pc.localisation.Ranges;
import rp.warehouse.pc.localisation.interfaces.Localisation;
import rp.warehouse.pc.management.providers.localisation.LocalisationListener;

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
	public static final byte[] directionProtocol = new byte[] { FORWARD, RIGHT, BACKWARD, LEFT };
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
	private final HashSet<Point> relativeVisitedPoints = new HashSet<>();
	private final List<LocalisationListener> listeners = new ArrayList<>();

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
		while (needsToRun(northAssumption, eastAssumption, southAssumption, westAssumption)
				&& runCounter++ < MAX_RUNS) {
			List<Byte> directions = ranges.getAvailableDirections();
			// Filter if it can go somewhere
			if (directions.size() > 0) {
				List<Byte> tempDirections = new ArrayList<>(directions);
				// Remove backwards and all directions that would lead to visiting the same
				// point again.
				tempDirections.removeIf(d -> d == (byte) 2 || relativeVisitedPoints
						.contains(relativePoint.add(directionPoint[(previousDirection + d) % 4])));
				// If there are any directions left, set these as the directions to use,
				// otherwise use the old ones.
				if (tempDirections.size() > 0) {
					directions = tempDirections;
				}
			}
			logger.info("Available directions: " + directions);
			// Choose forwards, otherwise choose a random direction from the list of
			// available directions.
			final byte direction = directions.contains((byte) 0) ? 0
					: directions.get(random.nextInt(directions.size()));
			logger.info("Chosen direction: " + direction);
			previousDirection = (byte) ((previousDirection + direction) % 4);
			final Point move = directionPoint[direction];
			logger.info("Chosen move: " + move);

			// Move the robot
			comms.sendMovement(directionProtocol[previousDirection]);

			// Update relative position
			relativePoint = relativePoint.add(move);
			relativeVisitedPoints.add(relativePoint);
			logger.info("Previous direction: " + previousDirection);
			logger.info("Reversal rotation amount: " + direction);
			// Update ranges
			ranges = comms.getRanges();
			logger.info("Received ranges: " + ranges);

			northAssumption.update(direction, ranges);
			eastAssumption.update(direction, ranges);
			southAssumption.update(direction, ranges);
			westAssumption.update(direction, ranges);

			for (LocalisationListener listener : listeners) {
				listener.newPoints(getCurrentLocations());
			}
		}

		// One of the assumptions is complete, return the completed position
		final RobotLocation location = Stream.of(northAssumption, eastAssumption, southAssumption, westAssumption)
				.filter(LocalisationCollection::isComplete)
				.map(l -> new RobotLocation(l.getPoint(), directionProtocol[l.getHeading()])).findFirst().get();
		logger.debug("Found location: " + location);
		return location;
	}

	/**
	 * Method to get a stream of all of the current Robot Locations.
	 * 
	 * @return a stream of all of the locations.
	 */
	public List<Stream<RobotLocation>> getCurrentLocations() {
		return Arrays.asList(northAssumption.stream(), eastAssumption.stream(), southAssumption.stream(),
				westAssumption.stream());
	}

	/**
	 * Method to determine whether the loop still needs to run.
	 * 
	 * @param assumptions
	 *            the different direction assumptions.
	 * @return whether the loop needs to run.
	 */
	private boolean needsToRun(LocalisationCollection... assumptions) {
		return Stream.of(assumptions).mapToInt(LocalisationCollection::getNumberOfPoints).sum() != 1;
	}

	/**
	 * Method to add a listener to the Localiser.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addListener(LocalisationListener listener) {
		listeners.add(listener);
	}

}
