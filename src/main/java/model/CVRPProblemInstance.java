package model;

public class CVRPProblemInstance {

    public CVRPProblemInstance(CVRPProblem base) {
        this.name = base.name;
        this.comment = base.comment;
        this.type = base.type;
        this.dimension = base.dimension;
        this.adjacencyMatrix = convertCoordinates(base.node_coordinates, base.edge_weight_type);
    }

    public String name;
    public String comment;
    public String type;
    public Integer dimension;
    public String edge_weight_type;
    public double[][] adjacencyMatrix;

    private static double[][] convertCoordinates(int[][] coords, String weightType) {

        double[][] ret = new double[coords.length][coords.length];

        switch(weightType) {
            case "EUC_2D":
                // O(n^2) :(
                for(int i = 0; i < coords.length; i++) {
                    for(int j = 0; j < coords.length; j++) {
                        // Distance from i -> j
                        ret[i][j] = Math.sqrt( Math.pow(coords[i][0] -coords[j][0],2) + Math.pow(coords[i][1] -coords[j][1], 2) );
                    }
                }
                break;


            default: 
                return null;
        }
        return ret;
    }
}