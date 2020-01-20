package util;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

import solver.Ant;
import util.model.ExportedRun;
import util.model.TSPLibTour;

public class FileHelper {

    private String probName;
    private Integer dimensions;
    private String runTimeInfo;
    private ExportedRun tourData;  

    public FileHelper(String prob, Integer dim) {
        runTimeInfo = prob + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm"));
        probName = prob;
        dimensions = dim;
        tourData = new ExportedRun();
    }

    public void writeStoredToursToFile() {
        try {

            // Store fully fledged statistical analysis file;
            if(!tourData.isEmpty()) {
                // And then store them as JSON for convenient use in python;
                String json = new Gson().toJson(this.tourData);

                File file = new File("output/" + (runTimeInfo + "-bestcost_" + (Integer)tourData.getBestTourCost() ) + "/ExportedRun.json");
                file.getParentFile().mkdirs(); // < Allows the File/FileWriter to create a directory;

                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(json);
                }
            }

            // And then store them as JSON for convenient use in python;
            String json = new Gson().toJson(new TSPLibTour(this.probName, this.dimensions, tourData.getBestTour(), tourData.getBestTourCost()));

            File file = new File("output/tspTours/" + (runTimeInfo + "-cost" + (Integer)tourData.getBestTourCost() ) + "/" + this.probName + "_tour.json");
            file.getParentFile().mkdirs(); // < Allows the File/FileWriter to create a directory;

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(json);
            }
            
        } catch (IOException e) {
            System.err.println("Error while writing Tours to file! :(\n");
            System.err.println(e);
        }
    }

    public List<Integer> getBestTour() {
        return tourData.getBestTour();
    }

    public Integer getBestTourCost() {
        return tourData.getBestTourCost();
    }

    public void storeTour(Integer genNum, List<Ant> ants, double avgTourCost, double[][] probs) {
        
        tourData.addGeneration(genNum, ants, avgTourCost, probs);
    }

    public void setMinIndex(Integer num) {
        tourData.setBestGenIndex(num);
    }
}
