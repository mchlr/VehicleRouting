package solver;

import java.util.*;

import model.CVRPProblemInstance;

public class Ant {
    private List<Integer> tour;
    private double tourCost;
    private double [][] probMat;
    private int capacity;
    private int load;

    public Ant(double [][] probMat, int capacity) {
        tourCost = 0;
        load = 0;
        tour = new ArrayList<>();
        this.probMat = probMat;
        this.capacity = capacity;
    }

    public void sovleProblem(CVRPProblemInstance prob) {

        if (probMat == null) {
            generateRandomTour(prob);
        } else {
            //ToDo Run prob - based tour
        }
    }
    public List<Integer> getTour() {
        return this.tour;
    }
    public double getTourCost() {
        return this.tourCost;
    }

    // Outsource this as a static method into a TourClass?
    private void generateRandomTour(CVRPProblemInstance prob) {
        this.tour.add(0);

        int i = 0;
        while(this.tour.size() < (prob.getDimensions()-1)) {
            int randJ = (int)(Math.random() * (prob.getDimensions()-1));

            if(!this.tour.contains((randJ))) {
                // Get the next customers demand;
                int demandJ = prob.getDemand(randJ);

                // Check if the capcity allows visiting the next customer, or if we have to visit the depot first;
                if(this.capacity < (this.load + demandJ)) {
                    tour.add(0);
                    this.load = 0; // Reset the current load after visiting the depot;
                }

                // visitedNodes.get(i) -> random(J)
                //this.tourCost += prob.getDistance(this.tour.get(i), randJ);
                this.tour.add(randJ);
                load += demandJ;

                i += 1;
            }
            else {}
        }

        System.out.println("TOUR DONE - BACK 3 DEPOT!");

        // Back to depot
        this.tour.add(0);

        calculateTourCost(prob);
    }

    private void calculateTourCost(CVRPProblemInstance prob) {
        // Calculate the total cost of the tour;
        for(int x = 0; x < tour.size()-1; x++) {
            tourCost += prob.getDistance(tour.get(x), tour.get(x+1));
        }
    }
}