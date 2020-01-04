package util;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

import solver.Ant;
import util.model.ExportedTour;

public class FileHelper {

    private static String runTimeInfo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm"));

    public static void writeToursToFile(List<Ant> ants, Integer genNum) {
        try {
            List<ExportedTour> toExport = new ArrayList<>();

            // Convert the ant's tour informations;
            for (Ant current : ants) {
                toExport.add(new ExportedTour(current));
            }
            // And then store them as JSON for convenient use in python;
            String json = new Gson().toJson(toExport);

            File file = new File("output" + runTimeInfo + "/tours_gen" + genNum + ".json");
            file.getParentFile().mkdirs(); // < Allows the File/FileWriter to create a directory;

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(json);
            }
        } catch (IOException e) {
            System.err.println("Error while writing Tours to file! :(\n");
            System.err.println(e);
        }
    }
}