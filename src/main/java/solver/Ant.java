package solver;

import java.util.*;

import model.CVRPProblemInstance;

public class Ant {
    private List<Integer> tour;
    private double tourCost;
    private double[][] probMat;
    private int capacity;
    private int load;

    public Ant(double[][] probMat, int capacity) {
        tourCost = 0;
        load = 0;
        tour = new ArrayList<>();
        this.probMat = probMat;
        this.capacity = capacity;
    }

    // Get-Methods;
    public List<Integer> getTour() {
        return this.tour;
    }

    public double getTourCost() {
        return this.tourCost;
    }

    // Starting Point;
    public void sovleProblem(CVRPProblemInstance prob) {

        if (probMat == null) {
            generateRandomTour(prob);
        } else {
            generateProbabilityBasedTour(prob);
        }

        calculateTourCost(prob);
    }

    private void generateProbabilityBasedTour(CVRPProblemInstance prob) {
        this.tour.add(0);
        int i = 0;

        while (getUniqueCount(this.tour) < (prob.getDimensions())) {
            if (i != 0) {
                generateStep(i, prob);

            } else {
                // Will only get hit in the first iteration;
                generateStep(0, prob);
            }

            // Move to next node;
            i = tour.get((tour.size() - 1));
        }

        // Return back to depot after all nodes have been visited;
        this.tour.add(0);
    }

    private void generateFirstStep(CVRPProblemInstance prob) {
        
        int modDim = prob.getDimensions()-1;

        double[] probWheel = new double[modDim];
        for (int x = 0; x < modDim; x++) {
            for (int y = x+1; y <= modDim; y++) {
                probWheel[x] += probMat[0][y];
            }
        }

        var wheelie = probWheel;

        double decision = Math.random();

        for (int d = 0; d < probWheel.length; d++) {
            boolean a = false;
            boolean b = false;
            
            if (d == probWheel.length - 1) {
                a = 0 < decision; // I guess thats always true^^
                b = decision <= probWheel[d];
            }
            else {
                a = probWheel[d + 1] < decision;
                b = decision <= probWheel[d];    
            }
            if (a && b) {

                // Also mod the vertex-index since the probabilities within probWheel regard the nodes 1 -> n (Without Depot!);
                // Glauben, dass wir das hier nicht brauchen;
                int dMod = d+1; 
                
                tour.add(dMod);
                load += prob.getDemand(dMod);
                System.out.println("Prob move from " + 0 + " -> " + dMod);
            }
        }
    }


    private void generateStep(int i, CVRPProblemInstance prob) {

        // Use modDim since we are ignoring the depot node (=> -1) for this;
        int modDim = prob.getDimensions()-1;
        //int dMod = -1;

        double[] probWheel = new double[modDim];
        for (int x = 0; x < modDim; x++) {
            for (int y = x+1; y <= modDim; y++) {
                probWheel[x] += probMat[(i)][y];
            }
        }

        double decision = Math.random();

        var debugWheel = probWheel;
        var debug = decision;
        

        // Init at 0 since probs doesnt know if one should visit the depot;
        for (int d = 0; d < probWheel.length; d++) {
            boolean a = false;
            boolean b = false;
            
            if (d == probWheel.length - 1) {
                a = 0 < decision; // I guess thats always true^^
                b = decision <= probWheel[d];
            }
            else {
                a = probWheel[d + 1] < decision;
                b = decision <= probWheel[d];    
            }
            if (a && b) {

                // Also mod the vertex-index since the probabilities within probWheel regard the nodes 1 -> n (Without Depot!);
                int dMod = d+1; 
                if (!tour.contains(dMod)) {
                    if (this.capacity >= (this.load + prob.getDemand(dMod))) {
                        tour.add(dMod);
                        load += prob.getDemand(dMod);
                        //System.out.println("Prob move from " + i + " -> " + dMod);
                    } else {
                        tour.add(0);
                        tour.add(dMod);
                        //System.out.println("Prob move from " + i + " -> 0");
                        load = 0;
                        load += prob.getDemand(dMod);
                        // System.out.println("Prob move from 0 -> " + dMod);
                    }
                    break;
                } else {
                    // Ant war schon an dem Knoten, der gewählt wurde
                    generateStep(i, prob);
                }

            }
        }
    }

    private void generateRandomStep(CVRPProblemInstance prob) {
        int randJ = (int) (Math.random() * prob.getDimensions());
        if (!tour.contains(randJ)) {
            tour.add(randJ);
            System.out.println("Rand moved from 0 -> " + tour.get(tour.size() - 1));
        }
        else {
            generateRandomStep(prob);
        }
    }

    private int getUniqueCount(List<Integer> tour) {
        return new HashSet<Integer>(tour).size();
    }

    private void calculateTourCost(CVRPProblemInstance prob) {
        // Calculate the total cost of the tour;
        for (int x = 0; x < tour.size() - 1; x++) {
            tourCost += prob.getDistance(tour.get(x), tour.get(x + 1));
        }
    }

    // Outsource this as a static method into a TourClass?
    private void generateRandomTour(CVRPProblemInstance prob) {
        this.tour.add(0);
        int i = 0;

        while (getUniqueCount(this.tour) < (prob.getDimensions())) {
            int randJ = (int) (Math.random() * (prob.getDimensions() - 1));

            if (!this.tour.contains((randJ))) {
                // Get the next customers demand;
                int demandJ = prob.getDemand(randJ);

                // Check if the capcity allows visiting the next customer, or if we have to
                // visit the depot first;
                if (this.capacity < (this.load + demandJ)) {
                    tour.add(0);
                    this.load = 0; // Reset the current load after visiting the depot;
                }

                // visitedNodes.get(i) -> random(J)
                // this.tourCost += prob.getDistance(this.tour.get(i), randJ);
                this.tour.add(randJ);
                load += demandJ;

                i += 1;
            } else {}
        }

        System.out.println("TOUR DONE - BACK 3 DEPOT!");

        // Back to depot
        this.tour.add(0);

        calculateTourCost(prob);
    }
}