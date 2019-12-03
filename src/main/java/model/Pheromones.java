package model;

import java.util.Arrays;
import java.util.*;

public class Pheromones {


    public double[][] phero;
    public double[][] adj;
    public double[][] wayprobs;
    public double[][] coords;
    public double sumWege;
    public double sumpartwege;
    public ArrayList<Integer> ilist = new ArrayList<Integer>();
    public ArrayList<Integer> jlist = new ArrayList<Integer>();


    public ArrayList<Integer> getIlist() {
        return ilist;
    }

    public ArrayList<Integer> getJlist() {
        return jlist;
    }

    public void cloneAdjMatrix(double [][] matrix){
        this.adj = matrix;
        System.out.println("---------Wurde geklont---------");
    }
    public void clonecoords(double [][] coords){
        this.coords = coords;
        System.out.println("---------Wurde geklont---------");
    }


    public int rnd() {
      //  System.out.println("-----------RND-------------");
      //  System.out.println(phero.length);
        int rand = (int)(Math.random() * (phero.length));
        System.out.println(rand);
        return rand;
    }




    public void initway(){
        System.out.println("-------INIT WAY------------");
        sumpartwege=0;
        sumWege=Math.pow(SummeWege(adj),-1.0);
        System.out.println(sumWege);

        double Q = (int)(Math.random() * (sumWege)+sumWege/2);

        while(sumpartwege < Q){


                    int i = rnd();
                    int j = rnd();



                    ilist.add(i);
                    jlist.add(j);

                    phero[i][j]=adj[i][j];

                    sumpartwege += phero[i][j];
                    System.out.println(sumpartwege+" von Gesamt : "+Q);


            }


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


    public double SummeWege(double[][] adj){

        sumWege = 0.0;
        for(int i = 0; i < adj.length; i++){
            for(int j=0; j < adj.length; j++){

                sumWege += adj[i][j] / 2.0;

            }
        }

    //    System.out.println("---------------Berechnet-------------");
    //    System.out.println("Summe:"+ sumWege);
    return 1/sumWege;
    }








    public double [][] updatePhero(){

        for (int i = 0; i < phero.length; i++){
            for (int j =0; j < phero.length; j++){

               phero[i][j] = (phero[i][j] !=1.0 ? (1/phero[i][j]) : 1) /(1/ SummeWege(this.adj))*1000;

             }
        }

        return phero;
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
