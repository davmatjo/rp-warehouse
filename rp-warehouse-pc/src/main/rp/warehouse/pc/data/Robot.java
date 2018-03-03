package rp.warehouse.pc.data;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.route.RobotController;

import java.util.Queue;

public class Robot {

    private final String ID;
    private final String name;
    private final Communication comms;
    private Queue<Integer> route;
    private Location location;
    private int currentItem;
    private boolean fail = false;
    private RobotController controller;

    public Robot(String ID, String name) {
        this.ID = ID;
        this.name = name;
        this.comms = new Communication(ID, name, this);
    }

    public void addController(RobotController controller) {
        this.controller = controller;
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public boolean move() {
        // TODO - Fix this
        Integer nextMovement = route.poll();
        comms.sendMovement(nextMovement);
        return true;
    }

    public Queue<Integer> getRoute() {
        return route;
    }

    /**
     * When called cancels Job of the current item
     */
    public void cancelJob() {
        // currentItem.getJob()
        // cancelltems.add(getCurrentItem())
    }
}
