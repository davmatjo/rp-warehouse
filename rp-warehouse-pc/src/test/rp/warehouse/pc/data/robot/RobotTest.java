package rp.warehouse.pc.data.robot;

import org.junit.Test;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;

public class RobotTest {
    public Robot robot ;
    public void setup() {
        Queue<Task> items= new LinkedList<Task>(Arrays.asList(new Task(new Item(2.0f,3.0f), 2, "d2")));
        ExecutorService pool = Executors.newFixedThreadPool(1 * 2);
        try {
            robot = new Robot("0016531AFBE1", "ExpressBoi", items, pool, new RobotLocation(0, 0, 3));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void locationTest() {
        setup();
        
    }

}
