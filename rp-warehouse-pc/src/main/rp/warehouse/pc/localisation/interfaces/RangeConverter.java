package rp.warehouse.pc.localisation.interfaces;

/**
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
