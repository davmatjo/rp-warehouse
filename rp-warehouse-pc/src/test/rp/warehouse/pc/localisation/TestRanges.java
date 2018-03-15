package rp.warehouse.pc.localisation;

import org.junit.Assert;
import org.junit.Test;

public class TestRanges {

	@Test
	public void rangeRotationSingleValue() {
		Ranges r1 = new Ranges(true, false, false, false);
		Ranges r2 = new Ranges(false, true, false, false);
		Assert.assertEquals(r2, Ranges.rotate(r1, 1));
		Assert.assertEquals(r1, Ranges.rotate(r2, 3));
	}

	@Test
	public void rangeRotationMultipleValues() {
		Ranges r1 = new Ranges(true, true, false, false);
		Ranges r2 = new Ranges(false, true, true, false);
		Assert.assertEquals(r2, Ranges.rotate(r1, 1));
		Assert.assertEquals(r1, Ranges.rotate(r2, 3));
	}

	@Test
	public void rangeRotationMultipleValues2() {
		Ranges r1 = new Ranges(true, true, true, false);
		Ranges r2 = new Ranges(false, true, true, true);
		Assert.assertEquals(r2, Ranges.rotate(r1, 1));
		Assert.assertEquals(r1, Ranges.rotate(r2, 3));
	}

	@Test
	public void rangeRotationMultipleValues3() {
		Ranges r1 = new Ranges(true, true, true, true);
		Ranges r2 = new Ranges(true, true, true, true);
		Assert.assertEquals(r2, Ranges.rotate(r1, 1));
		Assert.assertEquals(r1, Ranges.rotate(r2, 3));
	}

}
