package rp.warehouse.pc.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import rp.warehouse.pc.route.RobotsControl;

public abstract class RobotMain {
    public static void main(String[] args) {
        ArrayList<Queue<Task>> x = new ArrayList<Queue<Task>>();
        Queue<Task> q  =new LinkedList<Task>();
        Item i = new Item(12.0f , 5.0f);
        Task t = new Task(i, 1);
        q.add(t);
        x.add(q);
        x.add(q);
        RobotsControl.addRobots(x);
    
    }

}
