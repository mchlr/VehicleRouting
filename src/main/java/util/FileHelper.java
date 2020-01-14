package util;


import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;


import solver.Ant;
import util.model.ExportedTour;

public class FileHelper {

    private String runTimeInfo;
    private List<ExportedTour> tourData;

    public FileHelper(String prob) {
        runTimeInfo =  prob + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm"));
        tourData = new ArrayList<>();
    }

    public void writeStoredToursToFile() {
        try {
            for(int i = 0; i < this.tourData.size(); i++) {
                // And then store them as JSON for convenient use in python;
                String json = new Gson().toJson(this.tourData.get(i));

                File file = new File("output/" + runTimeInfo + "/gen_" + i + ".json");
                file.getParentFile().mkdirs(); // < Allows the File/FileWriter to create a directory;

                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(json);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while writing Tours to file! :(\n");
            System.err.println(e);
        }
    }

    public void storeTour(List<Ant> ants, Double averageCost, double[][] pheroMatrix, Integer genNum) {
        tourData.add(new ExportedTour(ants, averageCost, pheroMatrix));
    }

    public Map<List<List<Integer>>, List<Double>> getTour(Integer genNum) {
        var data = tourData.get(genNum);
        return Map.of(data.antTour, data.tourCost);
    }
}