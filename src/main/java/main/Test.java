package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import model.CVRPProblem;
import model.CVRPProblemInstance;

import solver.ACOSolver;

public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println("---------- Starting ----------");

        Gson myGS = new Gson();

        // Note base-path on mac os is "/VehicleRouting/"";

        // String fileName = "data/att48.json";
        //String fileName = "data/att48.json";
        String fileName = "data/AN38k5.json";

        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String json = "";

        while ((line = br.readLine()) != null) {
            json += line;
        }
        br.close();

        CVRPProblem test = myGS.fromJson(json, CVRPProblem.class);

        CVRPProblemInstance bier = new CVRPProblemInstance(test);

        // -----PARAMETER for AOC-------//
        int iterCount = 5000;
        int antAmount = 15;
        int topAntCount = 5;

        // Terminate the algorithm if the best solution isn't undershot after n-Iterations;
        int terminateAfterNoBetterForIterations = 500;

        double pheroValue = 10000.0;
        // ------ weights for Probabilitycalculation ------ //
        double alpha = 1; // weight Pheromonvalue
        double beta = 1; // weight Attractivness
        double gamma = 1; // weight Favoability
        // ------ parameters for Vaporation ------ //
        double roh = 0.8;
        double theta = 80;

        ACOSolver mySolv = new ACOSolver(bier, iterCount, terminateAfterNoBetterForIterations, antAmount, topAntCount, pheroValue, alpha, beta, gamma, roh,
                theta);

        mySolv.solve();

        System.out.println("---------- DONE ----------");
    }
}
