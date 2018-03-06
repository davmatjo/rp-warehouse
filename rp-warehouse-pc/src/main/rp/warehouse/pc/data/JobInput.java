package rp.warehouse.pc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JobInput {

    //locations where the files to be read are stored
    private String itemsLocation;
    private String jobsLocation;
    private String locationsLocation;
    private String cancellationsLocation;
    private static String home = System.getProperty("user.home");

    //create the objects that will hold the items/jobs/etc
    Items items = new Items();

    //will be passed the location of the files as strings and recieve the files?
    JobInput(String itemsLocation, String jobsLocation, String locationsLocation, String cancellationsLocation) {
        this.itemsLocation = itemsLocation;
        this.jobsLocation = jobsLocation;
        this.locationsLocation = locationsLocation;
        this.cancellationsLocation = cancellationsLocation;
    }


    public void readItems() throws FileNotFoundException {

        //read in items, create the items and store them in the items class
        Scanner scanner = new Scanner(new File(home + "/Documents/rp-warehouse/rp-warehouse-pc/src/main/rp/warehouse/pc/data/items.csv"));

        //for every line
        while(scanner.hasNext()){

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //create a new item where:
            //1st string = name, 2nd string = reward, 3rd string = weight
            Item item = new Item(Float.parseFloat(arrayList.get(1)), Float.parseFloat(arrayList.get(2)));

            //add the item to the items table
            items.add(arrayList.get(0), item);

        }
        scanner.close();

    }

    public void readLocations() throws FileNotFoundException {

        //read in locations, create the locations and add them to the appropriate item
        Scanner scanner = new Scanner(new File(home + "/Documents/rp-warehouse/rp-warehouse-pc/src/main/rp/warehouse/pc/data/locations.csv"));

        //for every line
        while(scanner.hasNext()){

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //create a new location where:
            //2nd string = x, 3rd string = y
            Location location = new Location(Integer.parseInt(arrayList.get(1)), Integer.parseInt(arrayList.get(2)));

            //get the item from the items table where 1st string in array is the item name
            Item currentItem = items.getItem(arrayList.get(0));

            //add the location to the item
            currentItem.setLocation(location);


        }
        scanner.close();


    }

    public void readJobs() throws FileNotFoundException {

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


                //create tasks here and add them to the jobslist of thr current job
                //add the current job to the jobs class
                break;

            }

        }
        scanner.close();

    }




}
