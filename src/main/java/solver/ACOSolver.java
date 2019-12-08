package solver;

import java.util.*;

import model.CVRPProblemInstance;

public class ACOSolver {

    CVRPProblemInstance ref;
    private double[][] phero;

    private List<Ant> antInstances; 


    public ACOSolver(CVRPProblemInstance ref, int antAmount) {
        this.ref = ref;
        this.antInstances = new ArrayList<>();
        initializePheroMatrix();
        initailizeAnts(antAmount);
    }

    public void solve() {
        int antIdx = 0;
        for(Ant nxt : antInstances) {
            nxt.sovleProblem(this.ref);
            System.out.println("> Ant #" + antIdx + " generated a Route.");

            antIdx++;
        }

        // Generate a tour with each available ant;
        for(int i = 0; i < antInstances.size(); i++) {
            List<Integer> tour = antInstances.get(i).getTour();
            double tourCost = 0;

            // Calculate the tour costs/length for each ant;
            for(int j = 0; j < tour.size()-1; j++){
                tourCost += this.ref.getDistance(tour.get(i), tour.get(i+1));
            }
            antInstances.get(i).setTourCost(tourCost);
            System.out.println("Ant#" + i + " - Length: " + antInstances.get(i).getTourCost());
        }

        // TODO: Add heuristics here;


        // Update the pheromone-matrix
        System.out.println("PRE-UPDATE PheroMatrix:");
        System.out.println(Arrays.deepToString(this.phero));


        // TODO: Add function which gets the top-n Ants (top in regard to tourCost);
        // Just pass all the ants into the pheroDeposition
        pheroDeposition(antInstances);
        System.out.println("\n");

        System.out.println("POST-UPDATE PheroMatrix:");
        System.out.println(Arrays.deepToString(this.phero));


        // TODO: Pass the updated Phero-Matrix into an Ant after update;

    }

    private void pheroDeposition(List<Ant> topAnts) {
        int sig = topAnts.size();
        int lamb = 1;

        List<Integer> bestTour = null;
        double bestTourCost = Double.MAX_VALUE;
        for(Ant nxt : topAnts) {
            if(nxt.getTourCost() < bestTourCost) {
                bestTour = nxt.getTour(); 
                bestTourCost = nxt.getTourCost();
            }
        }

        for(Ant currAnt : topAnts) {
            List<Integer> antTour = currAnt.getTour();

            for(int i = 0; i < antTour.size()-1; i++) {
                double sigLamb = /* this.phero[antTour.get(i)][antTour.get(i+1)] +*/ (sig-lamb)/currAnt.getTourCost(); 
                
                // TODO: Check, if the current Arc (i -> i+1) is a part of bestTour;
                double sigStar = containsArc(bestTour, new int[]{antTour.get(i), antTour.get(i+1)} ) ? (sig/bestTourCost) : 0;

                this.phero[antTour.get(i)][antTour.get(i+1)] = this.phero[antTour.get(i)][antTour.get(i+1)] + sigLamb + sigStar;
            }
            lamb += 1;
        }
    }
    
    private boolean containsArc(List<Integer> bestTour, int[] targetArc) {
        for(int y = 0; y < bestTour.size()-1; y++) {
            if(bestTour.get(y) == targetArc[0] && bestTour.get(y+1) == targetArc[1]) {
                return true;
            }
        }
        return false;
    }



    

    // Methods for initializing this class (Called within constructor);

    private void initializePheroMatrix() {
        this.phero = new double[this.ref.getDimensions()][this.ref.getDimensions()];

        // Initalize the Pheromone Matrix with all ones;
        for(int i = 0; i < this.phero.length; i++) {
            for(int j = 0; j < this.phero.length; j++) { 
                this.phero[i][j] = 1.0;
            }
        }
    }

    private void initailizeAnts(int n) {
        for(int i = 0; i < n; i++) {
            this.antInstances.add(new Ant());
        }
    }


}