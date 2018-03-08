//has a hashmap that holds all the items

package rp.warehouse.pc.data;

import java.util.HashMap;

public class Items {

    private HashMap<String, Item> userTable = new HashMap<String, Item>();

    //method to add a new item
    public void add(String name, Item item) {
        userTable.put(name, item);
    }

    //method which returns an item
    public Item getItem(String name) {
        return userTable.get(name);
    }


}
