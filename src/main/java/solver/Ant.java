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

    public List<Integer> getTour() {
        return this.tour;
    }

    public double getTourCost() {
        return this.tourCost;
    }

    public void sovleProblem(CVRPProblemInstance prob) {

        if (probMat == null) {
            generateRandomTour(prob);
        } else {
            // ToDo Run prob - based tour
            generateProbabilityBasedTour(prob);
        }
    }

    private void generateProbabilityBasedTour(CVRPProblemInstance prob) {
        this.tour.add(0);
        int i = 0;

        var elemCount = this.tour.size();

        while ((this.tour.size() == 1 ? 1 : getUniqueCount(this.tour)) < (prob.getDimensions() - 1)) {

            if (i != 0) {
                generateStep(i, prob);

            } else {
                generateRandomStep(prob);
            }

            // Move to next node;
            i = tour.get((tour.size() - 1));
        }

    }

    private void generateStep(int i, CVRPProblemInstance prob) {
        
        double[] probWheel = new double[prob.getDimensions()];
        for (int x = 0; x < prob.getDimensions(); x++) {
            for (int y = x; y < prob.getDimensions(); y++) {
                probWheel[x] += probMat[i][y];
            }
        }

        double decision = Math.random();

        // Init at 0 since probs doesnt know if one should visit the depot;
        for (int d = 1; d < probWheel.length - 1; d++) {
            if (probWheel[d + 1] < decision && decision <= probWheel[d]) {
                if (!tour.contains(d)) {
                    if (this.capacity >= (this.load + prob.getDemand(d))) {
                        tour.add(d);
                        load += prob.getDemand(d);
                        System.out.println("Prob move from " + i + " -> " + d);
                    } else {
                        tour.add(0);
                        tour.add(d);
                        System.out.println("Prob move from " + i + " -> 0");
                        load = 0;
                        load += prob.getDemand(d);
                        System.out.println("Prob move from 0 -> " + d);
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
        int count = 0;

        var maxVal = getMaxValue(tour);

        boolean[] check = new boolean[maxVal];

        for (int i = 0; i < maxVal; i++) {
            if (!check[i]) {
                check[i] = true;
                count++;
            }
        }

        return count;
    }

    private int getMaxValue(List<Integer> tour) {
        Integer max = Integer.MIN_VALUE;

        for (int i = 0; i < tour.size(); i++) {
            if (max < tour.get(i)) {
                max = tour.get(i);
            }
        }
        return max;
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
            } else {
            }
        }

        System.out.println("TOUR DONE - BACK 3 DEPOT!");

        // Back to depot
        this.tour.add(0);

        calculateTourCost(prob);
    }
}