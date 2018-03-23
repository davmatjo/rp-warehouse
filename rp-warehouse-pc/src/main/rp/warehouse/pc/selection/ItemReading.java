package rp.warehouse.pc.selection;

import java.io.*;
import java.util.*;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;

/**
 * Used to read the items.csv appropriately in order to create the ARFF file.
 * @author nikollevunlieva
 *
 */

public class ItemReading {
	public static HashMap<String, Item> readItems(String ifile, String lfile) {
		BufferedReader ireader;
		BufferedReader lreader;
		String coma = ",";
		HashMap<String, Item> items = new HashMap<String, Item>();
		
		try {
			ireader = new BufferedReader(new FileReader(ifile));
			lreader = new BufferedReader(new FileReader(lfile));
			String iline;
			String lline;
			while ((iline = ireader.readLine()) != null && (lline = lreader.readLine()) != null) {
				String[] wr = iline.split(coma);
				String[] l = lline.split(coma);
				String iname = wr[0];
				float r = Float.parseFloat(wr[1]);
				float w = Float.parseFloat(wr[2]);
				Location location = new Location(Integer.parseInt(l[0]), Integer.parseInt(l[1]));
				Item i = new Item(iname, r, w, location);
				items.put(iname, i);
			}
			
			ireader.close();
			lreader.close();
			return items;
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return null;
		} catch (IOException e) {
			System.out.println("IO Failed");
			return null;
		}
	}
}


