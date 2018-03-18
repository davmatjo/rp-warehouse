package rp.warehouse.pc.localisation.interfaces;

/**
 * An interface for a generic range converter, which takes in values read in
 * different scenarios and converts them as though they were taken from within
 * the grid of the warehouse map.
 * 
 * @author Kieran
 *
 */
public interface RangeConverter {

	/**
	 * Convert the given range into a value sutiable for the Ranges class
	 * 
	 * @param range
	 *            The range to convert
	 * @return The converted value
	 */
	public boolean toGrid(float range);

}
