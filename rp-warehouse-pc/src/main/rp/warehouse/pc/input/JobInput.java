package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RewardCounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Megan
 */
public class JobInput {

    //locations where the files to be read are stored
    private String itemsPath;
    private String jobsPath;
    private String locationsPath;
    private String cancellationsPath;
    private static String home = System.getProperty("user.home");

    //create the objects that will hold the items/jobs/etc
    Items items = new Items();
    Jobs jobs = new Jobs();
    CancelledJobs cancelledJobs = new CancelledJobs();

    /**
     * returns the items
     * @return
     */
    public Items getItems() {
        return items;
    }

    //will be passed the location of the files as strings and recieve the files?

    /**
     * Assigns the paths of the files, runs all the methods to read in the files
     * @param itemsPath
     * @param jobsPath
     * @param locationsPath
     * @param cancellationsPath
     * @throws FileNotFoundException
     */
    public JobInput(String itemsPath,String jobsPath, String locationsPath, String cancellationsPath ) throws FileNotFoundException {
        this.itemsPath = itemsPath;
        this.jobsPath = jobsPath;
        this.locationsPath = locationsPath;
        this.cancellationsPath = cancellationsPath;
        readItems();
        readLocations();
        readJobs();
        RewardCounter.setJobs(jobs);
    }

    /**
     * @return The jobs that have been read in
     */
    public Jobs getJobs() {
        return jobs;
    }

    /**
     * Method to read in the Items file line by line, converts it into arraylist and creates Item objects using information
     * @throws FileNotFoundException
     */
    public void readItems() throws FileNotFoundException {

        //read in items, create the items and store them in the items class
        Scanner scanner = new Scanner(new File(itemsPath));

        //for every line
        while (scanner.hasNext()) {

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //create a new item where:
            //1st string = name, 2nd string = reward, 3rd string = weight
            Item item = new Item(arrayList.get(0), Float.parseFloat(arrayList.get(1)), Float.parseFloat(arrayList.get(2)));

            //add the item to the items table
            items.add(arrayList.get(0), item);

        }
        scanner.close();

    }

    /**
     * Method to read in the Locations file line by line, converts it into arraylist and creates locations objects using information
     * @throws FileNotFoundException
     */
    public void readLocations() throws FileNotFoundException {

        //read in locations, create the locations and add them to the appropriate item
        Scanner scanner = new Scanner(new File(locationsPath));

        //for every line
        while (scanner.hasNext()) {

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //create a new location where:
            //2nd string = x, 3rd string = y
            Location location = new Location(Integer.parseInt(arrayList.get(0)), Integer.parseInt(arrayList.get(1)));

            //get the item from the items table where 1st string in array is the item name
            Item currentItem = items.getItem(arrayList.get(2));

            //add (set) the location to the item
            currentItem.setLocation(location);


        }
        scanner.close();


    }

    /**
     * Method to read in the Jobs file line by line, converts it into arraylist and creates Job objects using information
     * @throws FileNotFoundException
     */
    public void readJobs() throws FileNotFoundException {

        //read in jobs, create the jobs and store them in the jobs class
        Scanner scanner = new Scanner(new File(jobsPath));
//        scanner.useDelimiter(",");

        //for every line
        while (scanner.hasNext()) {

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            //get the name of the job
            String name = arrayList.get(0);

            //remove the name from the list
            arrayList.remove(0);

            //create an arraylist to hold the tasks
            ArrayList<Task> tasks = new ArrayList<>();

            //while the arraylist isnt empty
            while (arrayList.size() > 0) {

                //get the item (find the item in items)
                Item item = items.getItem(arrayList.get(0));

                //get the count
                Integer count = Integer.parseInt(arrayList.get(1));

                //create a task from these two itmes
                Task task = new Task(item, count, name);

                //add tasks to the tasks lists
                tasks.add(task);

                arrayList.remove(0);
                arrayList.remove(0);


            }

            //create a Job
            Job job = new Job(name, tasks);

            //add the job to the jobs list
            jobs.addJob(job);

        }
        scanner.close();

    }

    /**
     * Method to read in the Cancellations file line by line, converts it into arraylist and creates Cancellations objects using information
     * @throws FileNotFoundException
     */
    public void readCancellations() throws FileNotFoundException {

        //read in cancellations
        Scanner scanner = new Scanner(new File(cancellationsPath));

        //for every line
        while (scanner.hasNext()) {

            String line = scanner.next();
            //split the above string into an array of strings:
            List<String> list = Arrays.asList(line.split(","));
            //convert that list to an arraylist to make it easier to work with
            ArrayList<String> arrayList = new ArrayList<>(list.size());
            arrayList.addAll(list);

            for (int i = 0; i < arrayList.size(); i++) {
                //first item = job, second item = cancelled
                if (arrayList.get(1).equals("1")) {
                    //get the job
                    Job job = jobs.getJob(arrayList.get(0));
                    //add it to cancelled jobs
                    cancelledJobs.addJob(job);
                }
            }


        }


    }


}
