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

        String fileName = "data/att48.json";
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String json = "";

        while((line = br.readLine()) != null) {
            json += line;
        }
        br.close();

        CVRPProblem test = myGS.fromJson(json, CVRPProblem.class);

        CVRPProblemInstance bier = new CVRPProblemInstance(test);

        System.out.println("---------- DONE ----------");


        //-----PARAMETER for AOC-------//

        int antAmount = 3;

        //------ weights for Probabilitycalculation ------ //
        double alpha = 1; //weight Pheromonvalue
        double beta = 1; // weight Attractivness
        double gamma = 1; // weight Favoability
        //------ parameters for Vaporation ------ //
        double roh = 0.8;
        double theta = 80;




        ACOSolver mySolv = new ACOSolver(bier, 3, 1,1,1, 0.8,80);
        mySolv.solve();




    }
}
