package rp.warehouse.pc.data.robot.utils;

import org.junit.Test;

/**
 * 
 * @author roman
 *
 */
public class RobotUtilsTest {
    
    @Test
    public void locationTest() {
        RobotUtils utils = new RobotUtils(new RobotLocation(2, 3, 3), "Bob");
        utils.updateLocation(3);
        
    }

}
