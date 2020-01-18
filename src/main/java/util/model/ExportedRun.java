package util.model;

import java.util.ArrayList;
import java.util.List;

import solver.Ant;

public class ExportedRun {


    public Integer bestGenerationIndex;
    public List<GenerationStats> allGenerations;

    public ExportedRun() {
        bestGenerationIndex = -1;
        allGenerations = new ArrayList<>();
    }

    public void setBestGenIndex(Integer num) {
        bestGenerationIndex = num;
    }

    public Integer getBestTourCost() {
        return (int)Math.round(allGenerations.get(bestGenerationIndex).allCosts.get(0));
    }
    

    // GenerationStats(Integer genNum, List<Ant> allAnts, Integer bestAntIdx, Double averageCost, double[][] probabilities) {

    public void addGeneration(Integer genNum, List<Ant> ants, double avgTourCost, double[][] probMatrix){
        allGenerations.add(new GenerationStats(genNum, ants, avgTourCost, probMatrix));
    }
}