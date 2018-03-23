package rp.warehouse.pc.data;

import rp.warehouse.pc.data.Location;

public class Item {

    private String name;
    private Float reward;
    private float weight;
    private Location location;

    /**
     * Assigns names/rewards/weight when the item is made
     * @param name
     * @param reward
     * @param weight
     */
    public Item(String name, Float reward, Float weight) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
    }

    /**
     * Constructor that also takes in the location of an item
     * @param name
     * @param reward
     * @param weight
     * @param location
     */
    public Item(String name, Float reward, Float weight, Location location) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
        this.location = location;
    }

    /**
     * sets the Location of the item accordingly
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * returns the reward of the item
     * @return
     */
    public Float getReward() {
        return reward;
    }

    /**
     * returns the items
     * @return
     */
    public float getWeight() {
        return weight;
    }

    /**
     * returns the location of the item
     * @return
     */
    public Location getLocation() {
        return location;
    }

    @Override
    /**
     * returns the item as a string
     */
    public String toString() {
        return "at " + location + " of weight " + weight + " with reward " + reward;
    }

    /**
     * returns the name of the item
     * @return
     */
    public String getName() {
        return name;
    }
}
