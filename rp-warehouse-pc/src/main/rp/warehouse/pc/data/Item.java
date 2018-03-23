package rp.warehouse.pc.data;

public class Item {

    private String name;
    private Float reward;
    private float weight;
    private Location location;

    /**
     * Assigns names/rewards/weight when the item is made
     * @param name name of the item
     * @param reward reward of the item
     * @param weight weight of the item
     */
    public Item(String name, Float reward, Float weight) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
    }

    /**
     * Constructor that also takes in the location of an item
     * @param name name of the item
     * @param reward reward of the item
     * @param weight weight of the item
     * @param location location of the item
     */
    public Item(String name, Float reward, Float weight, Location location) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
        this.location = location;
    }

    /**
     * Creats an item from an item
     * @param item item to copy
     */
    public Item(Item item) {
        this.name = item.name;
        this.reward = item.reward;
        this.weight = item.weight;
        this.location = item.location;
    }

    /**
     * sets the Location of the item accordingly
     * @param location location of the item
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the reward of the item
     */
    public Float getReward() {
        return reward;
    }

    /**
     * @return weight of the item
     */
    public float getWeight() {
        return weight;
    }

    /**
     * @return location of the item
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return A string containing the relevant item information
     */
    @Override
    public String toString() {
        return "at " + location + " of weight " + weight + " with reward " + reward;
    }

    /**
     * @return name of the item
     */
    public String getName() {
        return name;
    }
}
