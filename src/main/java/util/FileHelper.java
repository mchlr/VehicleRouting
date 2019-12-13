package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import solver.Ant;

public class FileHelper {


    public static void writeToursToFile(List<Ant> ants) {
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            // System.out.println("Current relative path is: " + s);

            BufferedWriter wr = new BufferedWriter(new FileWriter(new File("output/iter.txt")));
            wr.close();
            // System.out.println("Pheromon-Matrix stored in File!");
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("Error while writing Phero-Matrix to file! :(\nContinueing...");
        }
    }
}