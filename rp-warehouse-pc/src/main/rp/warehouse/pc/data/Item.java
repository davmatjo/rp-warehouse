package rp.warehouse.pc.data;

public class Item {

    private String name;
    private Float reward;
    private float weight;
    private Location location;

    public Item(String name, Float reward, Float weight) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
    }

    public Item(String name, Float reward, Float weight, Location location) {
        this.name = name;
        this.reward = reward;
        this.weight = weight;
        this.location = location;
    }

    public Item(Item item) {
        this.name = item.name;
        this.reward = item.reward;
        this.weight = item.weight;
        this.location = new Location(item.location);
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

    //method to set the location of the item
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "at " + location + " of weight " + weight + " with reward " + reward;
    }

    public String getName() {
        return name;
    }
}
