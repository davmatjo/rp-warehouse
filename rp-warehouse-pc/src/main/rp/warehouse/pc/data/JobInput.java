package rp.warehouse.pc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    public static void main(String[] args) throws FileNotFoundException {

        String home = System.getProperty("user.home");

        //read in all the items, create item objects and store them in items class 


        //read in jobs, create the jobs and store them in the jobs class
        Scanner scanner = new Scanner(new File(home + "/Documents/rp-warehouse/rp-warehouse-pc/src/main/rp/warehouse/pc/data/jobs.csv"));
//        scanner.useDelimiter(",");

        //for every line
        while(scanner.hasNext()){

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //create a new job using the first item in the arraylist as the name
            Job job = new Job(arrayList.get(0));
            System.out.println("job name: " + arrayList.get(0));
            //remove the name from the list
            arrayList.remove(0);

            //while the arraylist isnt empty
            while (arrayList.size() != 0) {

               break;

            }

        }
        scanner.close();
    }




}
