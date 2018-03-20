package rp.warehouse.pc.data.robot.utils;


import org.apache.log4j.Logger;

import rp.warehouse.pc.communication.Protocol;
/**
 * Used to update location 
 * @author roman
 *
 */
public class RobotUtils{
    private RobotLocation location;
    private String name;
    
    private static final Logger logger = Logger.getLogger(RobotUtils.class);
    
    public RobotUtils(RobotLocation _location, String _name) {
        location = _location;
        name = _name;
    }
    
    /**
     * Updates Location based on the instruction 
     * @param lastInstruction - the instruction executed
     */
    public void updateLocation(int lastInstruction) {
        if (lastInstruction != -1) {
            logger.debug(name + ": " + "Updating location");

            location.setDirection(lastInstruction);
            location.forward();
            logger.info(name + ": " + "Current location X: " + location.getX() + " Y: " + location.getY());
        }
    }

}
