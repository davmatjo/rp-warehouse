package rp.warehouse.pc.selection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import rp.warehouse.pc.data.Item;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Prediction {
    public static void main(String[] args) {
        Logger log = Logger.getRootLogger();
        log.setAdditivity(false);
        BasicConfigurator.configure();

        String jfile = "jobs.csv";
        String trainfile = "training_jobs.csv";

        String wrfile = "items.csv";
        String lfile = "locations.csv";

        String cfile = "cancellations.csv";

        log.debug("Started reading item files...");

        HashMap<String, Item> itemMap = ItemReading.parseItems(wrfile, lfile);
        log.debug("Successfully read " + itemMap.size() + " items!");

        log.debug("Creating WEKA training set ARFF.");
        JobTraining.makeARFF(trainfile, cfile, itemMap, "training.arff");
        log.debug("Creating WEKA job set ARFF.");
        JobTraining.makeARFF(jfile, itemMap, "jobs.arff");
        log.debug("WEKA files succsessfully created.");

        try {
            log.debug("Reading ARFF file to training set.");
            DataSource tsource = new DataSource("training.arff");
            Instances tdata = tsource.getDataSet();
            tdata.setClass(tdata.attribute("cancelled"));
            log.debug("Successfully created training set.");

            DataSource jsource = new DataSource("jobs.arff");
            Instances jdata = jsource.getDataSet();

            log.debug("Successfully created test set.");

            AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
            Bagging bc = new Bagging();
            bc.setClassifier(new REPTree());
            classifier.setClassifier(bc);
            classifier.setEvaluator(new GainRatioAttributeEval());
            classifier.setSearch(new Ranker());
            classifier.buildClassifier(tdata);

            Instances newData = jdata;

            newData.setClass(newData.attribute("cancelled"));

            List<String> classified = new ArrayList<>();

            log.debug("Starting predictions.");
            for (int i = 0; i < newData.numInstances(); i++) {
                Instance j = newData.instance(i);
                classified.add(i + "," + classifier.classifyInstance(j));
            }

            ArffSaver sj = new ArffSaver();
            sj.setFile(new File("files/newJobs.arff"));
            sj.setInstances(jdata);
            sj.writeBatch();

            ArffSaver s = new ArffSaver();
            s.setFile(new File("files/newTraining.arff"));
            s.setInstances(tdata);
            s.writeBatch();

            File file = new File("prediction.csv");
            Files.write(Paths.get("prediction.csv"), classified, Charset.defaultCharset());

            log.debug("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
