package rp.warehouse.nxt.motion;

public interface Movement {
    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    boolean move(Direction direction);
}
