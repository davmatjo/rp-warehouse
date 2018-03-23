//has a hashmap that holds all the items

package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Item;

import java.util.HashMap;

/**
 * @author megan
 */
public class Items {

    private HashMap<String, Item> items = new HashMap<String, Item>();

    /**
     * adds an item to the hashmap
     * @param name
     * @param item
     */
    public void add(String name, Item item) {
        items.put(name, item);
    }

    /**
     * returns an item by a given name
     * @param name
     * @return
     */
    public Item getItem(String name) {
        return items.get(name);
    }

    /**
     * returns the hashmap of items
     * @return
     */
    public HashMap<String, Item> getItems() {
        return items;
    }


}
