//has an arraylist that holds all the items

package rp.warehouse.pc.data;

import java.util.ArrayList;

public class Items {

    ArrayList<Item> items = new ArrayList<Item>();

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }


}
