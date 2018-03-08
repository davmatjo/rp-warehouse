package rp.warehouse.pc.data;

import rp.warehouse.pc.data.Location;

public class Item {

    private Float reward;
    private float weight;
    private Location location;

    Item(Float reward, Float weight) {
        this.reward = reward;
        this.weight = weight;
    }

    //method to set the location of the item
    public void setLocation(Location location) {
        this.location = location;
    }

    //method to return the items reward
    public Float getReward() {
        return reward;
    }

    //method to return the item's weight
    public float getWeight() {
        return weight;
    }

    public Location getLocation() {
        return location;
    }
}
