package util;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

import solver.Ant;
import util.model.ExportedRun;

public class FileHelper {

    private String runTimeInfo;
    private ExportedRun tourData;

    public FileHelper(String prob) {
        runTimeInfo = prob + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm"));
        tourData = new ExportedRun();
    }

    public void writeStoredToursToFile() {
        try {
            // And then store them as JSON for convenient use in python;
            String json = new Gson().toJson(this.tourData);

            File file = new File("output/" + runTimeInfo + ".json");
            file.getParentFile().mkdirs(); // < Allows the File/FileWriter to create a directory;

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(json);
            }
        } catch (IOException e) {
            System.err.println("Error while writing Tours to file! :(\n");
            System.err.println(e);
        }
    }

    public void storeTour(Integer genNum, List<Ant> ants, double avgTourCost, double[][] probs) {
        tourData.addGeneration(genNum, ants, avgTourCost, probs);
    }

    public void setMinIndex(Integer num) {
        tourData.setBestGenIndex(num);
    }

    // public Map<List<List<Integer>>, List<Double>> getTour(Integer genNum) {
    // var data = tourData.getMinTour();
    // return Map.of(data.antTour, data.tourCost);
    // }

}