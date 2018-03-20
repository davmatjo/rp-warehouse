package rp.warehouse.pc.data.robot.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.data.Location;

/**
 * 
 * @author roman
 *
 */
public class RobotUtilsTest {
    
    @Test
    public void locationTest() {
        RobotLocation  location = new RobotLocation(2, 3, 3);
        RobotUtils utils = new RobotUtils(location, "Bob");
        utils.updateLocation(3);
        Assertions.assertEquals(new Location(2, 4), location);
        
    }

}
