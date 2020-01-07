package solver;

import java.util.*;
import java.util.concurrent.Callable;

import model.CVRPProblemInstance;
import util.MatrixHelper;

public class Ant implements Callable<Ant> {

    private CVRPProblemInstance problemRef;

    private List<Integer> tour;
    private double tourCost;
    private double[][] probMat;
    private int capacity;
    private int load;

    public Ant(double[][] probMat, int capacity, CVRPProblemInstance prob) {
        tourCost = 0;
        load = 0;
        tour = new ArrayList<>();
        this.probMat = probMat;
        this.capacity = capacity;
        this.problemRef = prob;
    }

    // Get-Methods;
    public List<Integer> getTour() {
        return this.tour;
    }

    public double getTourCost() {
        return this.tourCost;
    }

    // Starting Point;
    public void solve() {

        if (probMat == null) {
            generateRandomTour();
        } else {
            generateProbabilityBasedTour();
        }

        calculateTourCost();
    }

    private void generateProbabilityBasedTour() {
        this.tour.add(0);
        int i = 0;

        while (getUniqueCount(this.tour) < (problemRef.getDimensions())) {
            if (i != 0) {
                generateStep(i);

            } else {
                // Will only get hit in the first iteration;
                generateStep(0);
            }

            // Move to next node;
            i = tour.get((tour.size() - 1));
        }

        // Return back to depot after all nodes have been visited;
        this.tour.add(0);
    }

    private void generateStep(int i) {

        // Use modDim since we are ignoring the depot node (=> -1) for this;
        int modDim = problemRef.getDimensions()-1;
        //int dMod = -1;

        double[] probWheel = new double[modDim];

        // Calculate the column sum for normalizing the prob-wheel;
        Double probSum =0.0;
        for(int z = 0; z < probMat[(i)].length; z++) {
            probSum += probMat[(i)][z];
        }

        for (int x = 0; x < modDim; x++) {
            for (int y = x+1; y <= modDim; y++) {
                if(this.tour.contains(y)) {
                    continue;
                }
                else {
                    probWheel[x] += probMat[(i)][y]/probSum;
                }

                // probWheel[x] += probMat[(i)][y]/probSum;
            }
        }

        // double dec_A = Math.random()*100.0;
        // double dec_B = Math.round(dec_A);
        // double dec_C = dec_B/100.0;
        // double decision = dec_C;

        double decision = Math.random();
        decision = Math.round(100.0 * decision) / 100.0;

      //  System.out.println("Entscheidung:" + decision);

        var debugWheel = probWheel;    
        var debug = decision;
        
        // Init at 0 since probs doesnt know if one should visit the depot;
        for (int d = 0; d < probWheel.length; d++) {
            boolean a = false;
            boolean b = false;
            
            if (d == probWheel.length - 1) {
                a = 0 <= decision; // I guess thats always true^^
                b = decision <=Math.round(100.0 *  probWheel[d]) / 100.0;
            }
            else {
                a = Math.round(100.0 *  probWheel[d+1]) / 100.0 <= decision;
                b = decision <= Math.round(100.0 *  probWheel[d]) / 100.0    ;
            }
            if (a && b) {

                // Also mod the vertex-index since the probabilities within probWheel regard the nodes 1 -> n (Without Depot!);
                int dMod = d+1; 
                if (!tour.contains(dMod)) {
                    if (this.capacity >= (this.load + problemRef.getDemand(dMod))) {
                        tour.add(dMod);
                        load += problemRef.getDemand(dMod);
                        
                 //       System.out.println(">> New node reached!");
                    } else {
                        tour.add(0);
                        tour.add(dMod);
                        //System.out.println("Prob move from " + i + " -> 0");
                        load = 0;
                        load += problemRef.getDemand(dMod);
                        // System.out.println("Prob move from 0 -> " + dMod);
                    }
                    // System.out.println(Arrays.toString(probWheel));
                    break;
                } else {
                //   System.out.println(">> Already reached!");

                    // Ant war schon an dem Knoten, der gewählt wurde
                    generateStep(i);
                }

            }
            else {
             //   System.out.println(">> No Match!");
            }
        }
    }

    private void generateRandomStep() {
        int randJ = (int) (Math.random() * problemRef.getDimensions());
        if (!tour.contains(randJ)) {
            tour.add(randJ);
            System.out.println("Rand moved from 0 -> " + tour.get(tour.size() - 1));
        }
        else {
            generateRandomStep();
        }
    }

    private int getUniqueCount(List<Integer> tour) {
        return new HashSet<Integer>(tour).size();
    }

    private void calculateTourCost() {
        // Calculate the total cost of the tour;
        for (int x = 0; x < tour.size() - 1; x++) {
            tourCost += this.problemRef.getDistance(tour.get(x), tour.get(x + 1));
        }
    }

    // Outsource this as a static method into a TourClass?
    private void generateRandomTour() {
        this.tour.add(0);
        int i = 0;

        while (getUniqueCount(this.tour) < (this.problemRef.getDimensions())) {
            int randJ = (int) (Math.random() * (this.problemRef.getDimensions() - 1));

            if (!this.tour.contains((randJ))) {
                // Get the next customers demand;
                int demandJ = this.problemRef.getDemand(randJ);

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

        calculateTourCost();
    }
    
    @Override
    public Ant call() throws Exception {
        solve();
        return this;
    }
}