package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.List;

public class Ranges {

	public final static int FRONT = 0, RIGHT = 1, BACK = 2, LEFT = 3;
	private int[] ranges = new int[4];

	/**
	 * A class to store the ranges from all directions. Used in partnership with a
	 * location-oriented class such as Point or Location.
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
	public Ranges(final int up, final int right, final int down, final int left) {
		this.ranges[0] = up;
		this.ranges[1] = right;
		this.ranges[2] = down;
		this.ranges[3] = left;
	}

	/**
	 * Method to get a stored range in a given direction.
	 * 
	 * @param direction
	 *            The direction of which to return the range.
	 * @return The range in the given direction.
	 */
	public int get(final int direction) {
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
			dirs.add(FRONT);
		if (ranges[1] > 0)
			dirs.add(RIGHT);
		if (ranges[2] > 0)
			dirs.add(BACK);
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
		int[] store = new int[4];
		store[(FRONT + rot) % 4] = ranges.get(FRONT);
		store[(RIGHT + rot) % 4] = ranges.get(RIGHT);
		store[(BACK + rot) % 4] = ranges.get(BACK);
		store[(LEFT + rot) % 4] = ranges.get(LEFT);
		return new Ranges(store[0], store[1], store[2], store[3]);
	}

}
