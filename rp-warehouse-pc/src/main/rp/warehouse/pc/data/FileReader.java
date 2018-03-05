package rp.warehouse.pc.data;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("/Documents/rp-warehouse/rp-warehouse-pc/src/main/rp/warehouse/pc/data/Jobs.csv"));
        scanner.useDelimiter(",");
        while(scanner.hasNext()){
            System.out.print(scanner.next()+"|");
        }
        scanner.close();
    }




}
