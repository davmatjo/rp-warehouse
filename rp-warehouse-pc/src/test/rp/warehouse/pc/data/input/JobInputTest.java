package rp.warehouse.pc.data.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import rp.warehouse.pc.input.JobInput;

import java.io.FileNotFoundException;

public class JobInputTest {

    String itemsLocation = "./test/rp/warehouse/pc/data/input/itemsTest.csv";
    String locationsLocation = "./test/rp/warehouse/pc/data/input/locationsTest.csv";
    String jobsLocation = "./test/rp/warehouse/pc/data/input/jobsTest.csv";
    String cancellationsLocation = "./test/rp/warehouse/pc/data/input/cancellationsTest.csv";

    @Test
    public void shouldBeThreeItems() throws FileNotFoundException {
        JobInput jobInput = new JobInput(itemsLocation, locationsLocation, jobsLocation, cancellationsLocation);
        jobInput.items



    }



}
