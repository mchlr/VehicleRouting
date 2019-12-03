package model;

import java.util.Arrays;

public class Pheromones {


    public double[][] phero;
    public double[][] adj;
    public double[][] wayprobs;

    public void clone(double [][] matrix){
        this.adj = matrix;
        System.out.println("---------Wurde geklont---------");
    }


    public void initPhero(double[][] adj, double initValue) {
         this.phero = new double[adj.length][adj.length];

        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                this.phero[i][j] = initValue;
            }
        }
        System.out.println("---------Wurde initialisiert---------");
    }


    public void calcWayprobs(){

    //work in Progress


    }




    public void print() {
        for (double[] x : phero) {
            System.out.println(Arrays.toString(x));
        }
    }

    public void printclone() {
        for (double[] x : this.adj) {
            System.out.println(Arrays.toString(x));
        }
    }

}
