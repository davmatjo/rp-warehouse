package rp.warehouse.pc;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.route.RobotsControl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Task> myQ = new LinkedList<>();
        Item myItem = new Item(1f, 1f);
        myItem.setLocation(new Location(5, 5));
        myQ.offer(new Task(myItem, 1));

        ArrayList<Queue<Task>> myL = new ArrayList<>();
        myL.add(myQ);

        RobotsControl.addRobots(myL);
    }
}
