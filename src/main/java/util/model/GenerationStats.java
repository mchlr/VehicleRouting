package util.model;

import java.util.ArrayList;
import java.util.List;

import solver.Ant;

public class GenerationStats {
    public Integer generationNumber;
    public Double averageTourCost;
    public List<List<Integer>> allTours = new ArrayList<>();
    public List<Double> allCosts = new ArrayList<>();
    public double[][] probabilityMatrix;
    
    public GenerationStats() {
        generationNumber = -1;
        averageTourCost = -1.0;
        allTours = new ArrayList<>();
        allCosts = new ArrayList<>();
        probabilityMatrix = null;
    }

    public GenerationStats(Integer genNum, List<Ant> allAnts, Double averageCost, double[][] probabilities) {
        generationNumber = genNum;

        for(Ant ant : allAnts) {
            allTours.add(ant.getTour());
            allCosts.add(ant.getTourCost());
        }
        averageTourCost = averageCost; 
        probabilityMatrix = probabilities;
    }

    
}