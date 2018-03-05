package rp.warehouse.pc.data;

public class JobInput {

    //locations where the files to be read are stored
    String itemsLocation;
    String jobsLocation;
    String locationsLocation;
    String cancellationsLocation;

    //will be passed the location of the files as strings and recieve the files?
    JobInput(String itemsLocation, String jobsLocation, String locationsLocation, String cancellationsLocation) {
        this.itemsLocation = itemsLocation;
        this.jobsLocation = jobsLocation;
        this.locationsLocation = locationsLocation;
        this.cancellationsLocation = cancellationsLocation;
    }




}
