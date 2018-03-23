package rp.warehouse.nxt.motion;

/**
 * Movement interface
 * @author dxj786
 */
public interface Movement {
    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    boolean move(Direction direction);
}
