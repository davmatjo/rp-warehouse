package rp.warehouse.pc.selection;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.input.Job;
import java.util.*;
import java.io.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Used to convert from .cvs to .arff file. Made to match the read methods in JobReading
 * @author nikollevunlieva
 *
 */

public class JobTraining {
	public static void makeARFF(String jfile, String cfile, HashMap<String, Item> il, String arffFile) {
		try {
			HashMap<String, TrainedJob> training = JobReading.readTraining(jfile, cfile, il);
			ArrayList<String> itemNames = new ArrayList<String>(il.keySet());

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
				String dataLine = "";
				for (String s : itemNames) {
					if (j.getPicks().keySet().contains(s)) {
						dataLine += "1,";
					} else {
						dataLine += "0,";
					}
				}
				dataLine += j.totalWeight() + "," + j.totalReward() + "," + j.totalItems();
				if (j.isCancelled()) {
					dataLine += ",1";
				} else {
					dataLine += ",0";
				}
				wr.write(dataLine);
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
			ArrayList<String> itemNames = new ArrayList<String>(il.keySet());
			
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
				String dataLine = "";
				for (String s : itemNames) {
					if (j.getPicks().keySet().contains(s)) {
						dataLine += "1,";
					} else {
						dataLine += "0,";
					}
				}
				dataLine += j.totalWeight() + "," + j.totalReward() + "," + j.totalItems() + ",?";
				wr.write(dataLine);
				wr.newLine();
			}
			
			wr.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
