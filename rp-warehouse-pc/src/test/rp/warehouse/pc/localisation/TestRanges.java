package rp.warehouse.pc.localisation;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestRanges {

	@Test
	public void rangeRotationSingleValue() {
		Ranges r1 = new Ranges(true, false, false, false);
		Ranges r2 = new Ranges(false, true, false, false);
		Assertions.assertAll(() -> Assertions.assertEquals(r2, Ranges.rotate(r1, 1)),
				() -> Assertions.assertEquals(r1, Ranges.rotate(r2, 3)));
	}

	@Test
	public void rangeRotationMultipleValues() {
		Ranges r1 = new Ranges(true, true, false, false);
		Ranges r2 = new Ranges(false, true, true, false);
		Assertions.assertAll(() -> Assertions.assertEquals(r2, Ranges.rotate(r1, 1)),
				() -> Assertions.assertEquals(r1, Ranges.rotate(r2, 3)));
	}

	@Test
	public void rangeRotationMultipleValues2() {
		Ranges r1 = new Ranges(true, true, true, false);
		Ranges r2 = new Ranges(false, true, true, true);
		Assertions.assertAll(() -> Assertions.assertEquals(r2, Ranges.rotate(r1, 1)),
				() -> Assertions.assertEquals(r1, Ranges.rotate(r2, 3)));
	}

	@Test
	public void rangeRotationMultipleValues3() {
		Ranges r1 = new Ranges(true, true, true, true);
		Ranges r2 = new Ranges(true, true, true, true);
		Assertions.assertAll(() -> Assertions.assertEquals(r2, Ranges.rotate(r1, 1)),
				() -> Assertions.assertEquals(r1, Ranges.rotate(r2, 3)));
	}

	@Test
	public void rangeRotationRealWorld() {
		// Actual reading (east)
		Ranges r1 = new Ranges(false, true, true, true);
		// North-based reading
		Ranges r2 = new Ranges(true, false, true, true);
		Assertions.assertEquals(r2, Ranges.rotate(r1, 1));
	}

	@Test
	public void rangeRotationRealWorld2() {
		// Actual reading (west)
		Ranges r1 = new Ranges(false, false, true, true);
		// North-based reading
		Ranges r2 = new Ranges(false, true, true, false);
		Assertions.assertEquals(r2, Ranges.rotate(r1, 3));
	}

	@Test
	public void wrongRotation() {
		// Actual reading (south)
		Ranges r1 = new Ranges(false, false, true, false);
		// North reading
		Ranges r2 = new Ranges(true, false, false, false);
		Assertions.assertNotEquals(r2, Ranges.rotate(r1, 1));
	}

}
