package rp.warehouse.pc.route;

public class Robot {
    private final String ID;
    private Location location;
    private int currentJob;
    private final Communication comms;

    public Robot(String ID) {
        this.ID = ID;
        setLocation();
        this.comms = new Communication(ID);
    }

    private void setLocation() {
        this.location = new Location(1, 1, 1);
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    public void updateLocation(int directionTravelled) {

    }

    public int getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(int currentJob) {
        this.currentJob = currentJob;
    }

    public void move(int direction) {

    }
}
