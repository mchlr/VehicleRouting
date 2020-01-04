package util.model;

import java.util.List;

import solver.Ant;

public class ExportedTour {
    public double totalCost;
    public List<Integer> tour;

    public ExportedTour() {
        totalCost = -1;
        tour = null;
    }

    public ExportedTour(double cost, List<Integer> tour) {
        this.totalCost = cost;
        this.tour = tour;
    }

    public ExportedTour(Ant template) {
        this.totalCost = template.getTourCost();
        this.tour = template.getTour();
    }
}