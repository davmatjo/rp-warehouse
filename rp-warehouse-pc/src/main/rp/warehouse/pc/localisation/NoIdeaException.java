package rp.warehouse.pc.localisation;

/**
 * 
 * @author Kieran
 *
 */
public class NoIdeaException extends Exception {

	private static final long serialVersionUID = -2565935563233223381L;
	private final Ranges ranges;

	public NoIdeaException(final Ranges ranges) {
		this.ranges = ranges;
	}

	public Ranges getRanges() {
		return this.ranges;
	}

}
