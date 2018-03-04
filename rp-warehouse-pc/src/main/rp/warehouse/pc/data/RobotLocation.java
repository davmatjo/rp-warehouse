package rp.warehouse.pc.data;

public class RobotLocation extends Location {
    private int direction;

    public RobotLocation(int x, int y, int direction) {
        super(x, y);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
