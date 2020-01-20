package util.model;

import java.util.List;

public class TSPLibTour {

    public String name;
    public String type = "TOUR";
    public int dimensions;
    public List<Integer> tour;
    public int tourlength;
    public int lowerbound = 0;

    public TSPLibTour(String name, int dim, List<Integer> tour, int tourlength) {
        this.name = name;
        this.dimensions = dim;
        this.tour = tour;
        this.tourlength = tourlength;
    }
}