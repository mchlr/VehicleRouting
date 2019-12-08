package model;

public class CVRPProblem {

    public CVRPProblem() {
    }

    public String name;
    public String comment;
    public String type;
    public Integer dimension;
    public String edge_weight_type;
    public int[][] node_coordinates;
    public int[][] demand_section;
    public int[] depot_section;
}