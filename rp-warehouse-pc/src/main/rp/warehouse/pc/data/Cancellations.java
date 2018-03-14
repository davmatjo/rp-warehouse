package rp.warehouse.pc.data;

import rp.util.HashMap;

public class Cancellations {

    HashMap<Job, Integer> cancellations = new HashMap<Job, Integer>();

    public HashMap<Job, Integer> getCancellations() {
        return cancellations;
    }

    public void addCancellation(Job job, Integer cancelled) {
        cancellations.put(job, cancelled);

    }

}
