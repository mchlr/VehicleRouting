package util.model;

import java.util.ArrayList;
import java.util.List;

import solver.Ant;

public class ExportedTour {
    public List<Double> tourCost = new ArrayList<>();
    public List<List<Integer>> antTour = new ArrayList<>();
    public double[][] pheromons;
    public Double averageCost;

    public ExportedTour() {
        tourCost = null;
        antTour = null;
    }

    public ExportedTour(List<Ant> template, Double averageCost, double[][] pheromons) {
        for(Ant curr : template) {
            this.antTour.add(curr.getTour());
            this.tourCost.add(curr.getTourCost());
        }
        this.pheromons = pheromons;
        this.averageCost = averageCost;
    }
}