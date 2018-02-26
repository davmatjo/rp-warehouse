package rp.warehouse.nxt;

public interface Movement {
    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    boolean move(Direction direction);
}
