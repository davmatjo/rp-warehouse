package rp.warehouse.pc.selection;

import rp.warehouse.pc.data.Item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to convert from .cvs to .arff file. Made to match the read methods in JobReading
 * @author nikollevunlieva
 *
 */

public class JobTraining {
    public static void makeARFF(String jfile, String cfile, HashMap<String, Item> il, String arffFile) {
        try {
            HashMap<String, TrainedJob> training = JobReading.readTraining(jfile, cfile, il);
            ArrayList<String> itemNames = new ArrayList<>(il.keySet());

            BufferedWriter wr = new BufferedWriter(new FileWriter(arffFile));

            wr.write("% 1. Title: Job List - Warehouse\n");
            wr.write("%\n");
            wr.write("@RELATION job\n");
            wr.newLine();

            for (String s : itemNames) {
                wr.write("@ATTRIBUTE " + s + " {0, 1}\n");
            }
            wr.write("@ATTRIBUTE weight NUMERIC\n");
            wr.write("@ATTRIBUTE reward NUMERIC\n");
            wr.write("@ATTRIBUTE itemCount NUMERIC\n");
            wr.write("@ATTRIBUTE cancelled {0, 1}\n");
            wr.newLine();

            wr.write("@DATA\n");
            for (TrainedJob j : training.values()) {
                StringBuilder dataLine = new StringBuilder();
                for (String s : itemNames) {
                    if (j.getPicks().keySet().contains(s)) {
                        dataLine.append("1,");
                    } else {
                        dataLine.append("0,");
                    }
                }
                dataLine.append(j.totalWeight()).append(",").append(j.totalReward()).append(",").append(j.totalItems());
                if (j.isCancelled()) {
                    dataLine.append(",1");
                } else {
                    dataLine.append(",0");
                }
                wr.write(dataLine.toString());
                wr.newLine();
            }

            wr.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void makeARFF(String jfile, HashMap<String, Item> il, String arffFile) {
        try {
            HashMap<String, TrainedJob> training = JobReading.readTraining(jfile, il);
            ArrayList<String> itemNames = new ArrayList<>(il.keySet());

            BufferedWriter wr = new BufferedWriter(new FileWriter(arffFile));

            wr.write("% 1. Title: Job List - Warehouse\n");
            wr.write("%\n");
            wr.write("@RELATION job\n");
            wr.newLine();

            for (String s : itemNames) {
                wr.write("@ATTRIBUTE " + s + " {0, 1}\n");
            }

            wr.write("@ATTRIBUTE weight NUMERIC\n");
            wr.write("@ATTRIBUTE reward NUMERIC\n");
            wr.write("@ATTRIBUTE itemCount NUMERIC\n");
            wr.write("@ATTRIBUTE cancelled {0, 1}\n");
            wr.newLine();

            wr.write("@DATA\n");
            for (int i = 10000; i < 10100; i++) {
                TrainedJob j = training.get(Integer.toString(i));
                StringBuilder dataLine = new StringBuilder();
                for (String s : itemNames) {
                    if (j.getPicks().keySet().contains(s)) {
                        dataLine.append("1,");
                    } else {
                        dataLine.append("0,");
                    }
                }
                dataLine.append(j.totalWeight()).append(",").append(j.totalReward()).append(",").append(j.totalItems()).append(",?");
                wr.write(dataLine.toString());
                wr.newLine();
            }

            wr.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
