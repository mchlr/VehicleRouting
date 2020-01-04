package model;

public class CVRPProblemInstance {

    public CVRPProblemInstance(CVRPProblem base) {
        this.name = base.name;
        this.comment = base.comment;
        this.type = base.type;
        this.dimension = base.dimension;
        this.capacity = base.capacity;
        this.adjacencyMatrix = convertCoordinates(base.node_coordinates, base.edge_weight_type);
        this.demands = convertDemands(base.demand_section);
        this.depots = convertDepots(base.depot_section);
    }

    public String name;
    public String comment;
    public String type;
    public Integer dimension;
    public int capacity;
    public String edge_weight_type;
    public double[][] adjacencyMatrix;
    public int[] demands;
    public int[] depots;

    public Double getDistance(int i, int j) {
        // Always use the smaller idx as the col;
        return i < j ? this.adjacencyMatrix[i][j] : this.adjacencyMatrix[j][i];
    }

    public Integer getDimensions() {
        return this.dimension;
    }

    public Integer getDemand(int nIdx) {
        return this.demands[nIdx];
    }


    // ##################################################
    // # private convert-Functions for initialization   #
    // ##################################################
    private static double[][] convertCoordinates(int[][] coords, String weightType) {

        double[][] ret = new double[coords.length][coords.length];

        switch(weightType) {
            case "EUC_2D":
                // O(n^2) :(
                for(int i = 0; i < coords.length; i++) {
                    for(int j = i; j < coords.length; j++) {
                        // Distance from i -> j
                        ret[i][j] = Math.round(100.0 *  ( Math.sqrt( Math.pow(coords[i][0]-coords[j][0],2) + Math.pow(coords[i][1]-coords[j][1], 2)) )) / (100.0);;
                    }
                }
                break;

            default: 
                return null;
        }
        return ret;
    }

    private int[] convertDemands(int[][] demandMat) {
        int[] ret = new int[demandMat.length];
        int i = 0;
        for(int[] entry : demandMat) {
            ret[i] = entry[1];
            i++;
        }

        return ret;
    }

    private int[] convertDepots(int[] depotMat) {
        int[] ret = new int[depotMat.length];
        int i = 0;
        for(int entry : depotMat) {
            // This entry has to be subtracted by -1 to get "real" array indexes
            ret[i] = entry-1;
            i++;
        }

        return ret;
    }
}