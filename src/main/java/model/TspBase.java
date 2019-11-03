package model;

public class TspBase {
    public String name;
    public String comment;
    public String type;
    public int dimension;
    public String edgeWeightType;
    public Coord[] nodeCoords;

    public String toString(){
        return "hi from: " + name + " " + comment + " @Dimension: " + dimension;
    }
}

