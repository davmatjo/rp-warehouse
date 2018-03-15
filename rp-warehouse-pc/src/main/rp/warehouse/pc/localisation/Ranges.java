package rp.warehouse.pc.localisation;

import rp.warehouse.pc.localisation.implementation.PhysicalRangeConverter;
import rp.warehouse.pc.localisation.implementation.VirtualRangeConverter;
import rp.warehouse.pc.localisation.interfaces.RangeConverter;

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

	public final static byte UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	public final static RangeConverter virtualConverter = new VirtualRangeConverter();
	public final static RangeConverter physicalConverter = new PhysicalRangeConverter();
	private final static byte[] opposite = new byte[] { 2, 3, 0, 1 };
	private boolean[] ranges = new boolean[4];

	/**
	 * Create an instance of Ranges given the up, right, down and left ranges to be
	 * stored, converting real world or virtual world distances.
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
	public Ranges(final float up, final float right, final float down, final float left, RangeConverter converter) {
		this.ranges[0] = converter.toGrid(up);
		this.ranges[1] = converter.toGrid(right);
		this.ranges[2] = converter.toGrid(down);
		this.ranges[3] = converter.toGrid(left);
	}

	/**
	 * Create an instance of Ranges given the up, right, down and left ranges to be
	 * stored, using pre-converted values.
	 * 
	 * @param up
	 * @param right
	 * @param down
	 * @param left
	 */
	public Ranges(final boolean up, final boolean right, final boolean down, final boolean left) {
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
	public boolean get(final byte direction) {
		assert direction <= LEFT && direction >= UP : direction;
		return ranges[direction];
	}

	/**
	 * Method to get a list of available directions to travel in given the ranges.
	 * 
	 * @return The directions of which can be travelled without collisions.
	 */
	public List<Byte> getAvailableDirections() {
		List<Byte> dirs = new ArrayList<Byte>();
		if (ranges[0])
			dirs.add(UP);
		if (ranges[1])
			dirs.add(RIGHT);
		if (ranges[2])
			dirs.add(DOWN);
		if (ranges[3])
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
		final boolean[] store = new boolean[4];
		store[(UP + rot) % 4] = ranges.get(UP);
		store[(RIGHT + rot) % 4] = ranges.get(RIGHT);
		store[(DOWN + rot) % 4] = ranges.get(DOWN);
		store[(LEFT + rot) % 4] = ranges.get(LEFT);
		return new Ranges(store[0], store[1], store[2], store[3]);
	}

	/**
	 * 
	 * @param direction
	 * @return
	 */
	public static byte getOpposite(final byte direction) {
		assert direction >= 0 && direction <= 4 : direction;
		return opposite[direction];
	}

	public static Ranges fromArray(final float[] array, RangeConverter converter) {
		System.out.println(array[0] + ", " + array[1] + ", " + array[2] + ", " + array[3]);
		return new Ranges(converter.toGrid(array[0]), converter.toGrid(array[1]), converter.toGrid(array[2]),
				converter.toGrid(array[3]));
	}

	public static Ranges fromArray(final boolean[] array) {
		return new Ranges(array[0], array[1], array[2], array[3]);
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
			for (byte i = 0; i < 4; i++) {
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
