//has a hashmap that holds all the items

package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Item;

import java.util.HashMap;

public class Items {

    private HashMap<String, Item> items = new HashMap<String, Item>();

    //method to add a new item
    public void add(String name, Item item) {
        items.put(name, item);
    }

    //method which returns an item
    public Item getItem(String name) {
        return items.get(name);
    }

    public HashMap<String, Item> getItems() {
        return items;
    }


}
