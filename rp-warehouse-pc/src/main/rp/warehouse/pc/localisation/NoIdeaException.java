package rp.warehouse.pc.localisation;

/**
 * Exception that occurs when the robot can't figure out where it is.
 * 
 * @author Kieran
 *
 */
public class NoIdeaException extends Exception {

	private static final long serialVersionUID = -2565935563233223381L;
	private final Ranges ranges;

	/**
	 * Create a NoIdeaException given a set of related ranges.
	 * 
	 * @param ranges
	 *            the ranges related to causing this exception.
	 */
	public NoIdeaException(final Ranges ranges) {
		this.ranges = ranges;
	}

	/**
	 * Get the ranges related to the cause of this exception.
	 * 
	 * @return the related ranges to this exception.
	 */
	public Ranges getRanges() {
		return this.ranges;
	}

}
