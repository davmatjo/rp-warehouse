package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class to store the ranges from all directions. Used in partnership with a
 * location-oriented class such as Point or Location.
 * 
 * @author Kieran
 *
 */
public class Ranges {

	public final static int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private int[] ranges = new int[4];

	/**
	 * Create an instance of Ranges given the up, right, down and left ranges to be
	 * stored.
	 * 
	 * @param up
	 *            The range <b>upward</b> relative to the current position.
	 * @param right
	 *            The range <b>right</b> relative to the current position.
	 * @param down
	 *            The range <b>downward</b> relative to the current position.
	 * @param left
	 *            The range <b>left</b> relative to the current position.
	 */
	public Ranges(final float up, final float right, final float down, final float left) {
		this.ranges[0] = toGrid(up);
		this.ranges[1] = toGrid(right);
		this.ranges[2] = toGrid(down);
		this.ranges[3] = toGrid(left);
	}

	/**
	 * Method to get a stored range in a given direction.
	 * 
	 * @param direction
	 *            The direction of which to return the range.
	 * @return The range in the given direction.
	 */
	public int get(final int direction) {
		assert direction <= LEFT && direction >= UP : direction;
		return ranges[direction];
	}

	/**
	 * Method to get a list of available directions to travel in given the ranges.
	 * 
	 * @return The directions of which can be travelled without collisions.
	 */
	public List<Integer> getAvailableDirections() {
		List<Integer> dirs = new ArrayList<Integer>();
		if (ranges[0] > 0)
			dirs.add(UP);
		if (ranges[1] > 0)
			dirs.add(RIGHT);
		if (ranges[2] > 0)
			dirs.add(DOWN);
		if (ranges[3] > 0)
			dirs.add(LEFT);
		return dirs;
	}

	/**
	 * Method to rotate the ranges by a given angle.
	 * 
	 * @param ranges
	 *            The ranges to rotate.
	 * @param rot
	 *            The angle of rotation.<br>
	 *            0 = 0<br>
	 *            1 = 90<br>
	 *            2 = 180<br>
	 *            3 = 270
	 * @return The rotated version of the ranges.
	 */
	public static Ranges rotate(final Ranges ranges, final int rot) {
		assert rot >= 0 && rot <= 3 : rot;
		final int[] store = new int[4];
		store[(UP + rot) % 4] = ranges.get(UP);
		store[(RIGHT + rot) % 4] = ranges.get(RIGHT);
		store[(DOWN + rot) % 4] = ranges.get(DOWN);
		store[(LEFT + rot) % 4] = ranges.get(LEFT);
		return new Ranges(store[0], store[1], store[2], store[3]);
	}

	/**
	 * Method to convert a range reading to grid point distance - how many grid
	 * points are within the range.
	 * 
	 * @param range
	 *            The range reading from the sensor.
	 * @return The amount of grid points within that range.
	 */
	private static int toGrid(final float range) {
		final int gridRange = (int) Math.floor((range - 0.1f) / 0.3d);
		return gridRange < 0 ? 0 : gridRange;
	}

	@Override
	public String toString() {
		return "UP: " + ranges[0] + ", RIGHT: " + ranges[1] + ", DOWN: " + ranges[2] + ", LEFT: " + ranges[3];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Ranges)) {
			return false;
		} else {
			Ranges other = (Ranges) obj;
			for (int i = 0; i < 4; i++) {
				if (get(i) != other.get(i))
					return false;
			}
			return true;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(ranges[0], ranges[1], ranges[2], ranges[3]);
	}

}
