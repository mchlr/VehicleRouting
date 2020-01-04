package util.model;

import java.util.ArrayList;
import java.util.List;

import solver.Ant;

public class ExportedTour {
    public List<Double> tourCost = new ArrayList<>();
    public List<List<Integer>> antTour = new ArrayList<>();
    public Double averageCost;

    public ExportedTour() {
        tourCost = null;
        antTour = null;
    }

    public ExportedTour(List<Ant> template, Double averageCost) {
        for(Ant curr : template) {
            this.antTour.add(curr.getTour());
            this.tourCost.add(curr.getTourCost());
        }
        this.averageCost = averageCost;
    }
}