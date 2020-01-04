package util;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

import solver.Ant;
import util.model.ExportedTour;

public class FileHelper {



    public static void writeToursToFile(List<Ant> ants, Integer genNum) {
        try {
            List<ExportedTour> toExport = new ArrayList<>();

            // Convert the ant's tour informations;
            for (Ant current : ants) {
                toExport.add(new ExportedTour(current));
            }
            // And then store them as JSON for convenient use in python;
            String json = new Gson().toJson(toExport);

            try (FileWriter fw = new FileWriter(new File("output/tours_gen" + genNum + ".json"))) {
                fw.write(json);
            }
        } catch (IOException e) {
            System.err.println("Error while writing Tours to file! :(\n");
            System.err.println(e);
        }
    }
}