package rp.warehouse.pc;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.route.RobotsControl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Task> queue = new LinkedList<>();
        queue.add(new Task(new Item(1f, 1f), 1));
        queue.add(new Task(new Item(1f, 1f), 1));
        // TODO: more stuff
    }
}
