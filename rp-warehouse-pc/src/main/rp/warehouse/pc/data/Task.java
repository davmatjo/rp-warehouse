package rp.warehouse.pc.data;

import rp.warehouse.pc.data.Item;

public class Task {

    public final Item item;
    public final int count;

    public Task(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
} 