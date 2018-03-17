package rp.warehouse.pc.data.robot.utils;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.RobotLocation;

public class RobotUtils{
    private RobotLocation location;
    private String name;
    
    private static final Logger logger = Logger.getLogger(RobotUtils.class);
    
    public RobotUtils(RobotLocation _location, String _name) {
        location = _location;
        name = _name;
    }
    
    public void updateLocation(int lastInstruction) {
        if (lastInstruction != -1) {
            logger.debug(name + ": " + "Updating location");

            int x, y;
            x = location.getX();
            y = location.getY();
            int directionPointing = 0;

            // Works out the direction change
            switch (lastInstruction) {
            case Protocol.NORTH:
                y += 1;
                directionPointing = Protocol.NORTH;
                break;
            case Protocol.EAST:
                x += 1;
                directionPointing = Protocol.EAST;
                break;
            case Protocol.SOUTH:
                y -= 1;
                directionPointing = Protocol.SOUTH;
                break;
            case Protocol.WEST:
                x -= 1;
                directionPointing = Protocol.WEST;
                break;

            default:
                break;
            }
            location.setX(x);
            location.setY(y);
            location.setDirection(directionPointing);
            logger.info(name + ": " + "Current location X: " + location.getX() + " Y: " + location.getY());
        }
    }

}
