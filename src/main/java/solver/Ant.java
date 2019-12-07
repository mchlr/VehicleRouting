package solver;

import java.util.*;

import model.CVRPProblemInstance;

public class Ant {
    private List<Integer> tour;
    private double tourCost;

    public Ant() {
        tourCost = 0;
        tour = new ArrayList<>();
    }

    public void sovleProblem(CVRPProblemInstance prob) {
        generateRandomTour(prob);
    }

    public List<Integer> getTour() {
        return this.tour;
    }
    public double getTourCost() {
        return this.tourCost;
    }

    public void setTourCost(double cost) {
        this.tourCost = cost;
    }

    // Outsource this as a static method into a TourClass?
    private void generateRandomTour(CVRPProblemInstance prob) {
        this.tour.add(0);

        int i = 0;
        while(this.tour.size() < (prob.getDimensions()-1)) {
            int randJ = (int)(Math.random() * (prob.getDimensions()-1));

            if(!this.tour.contains((randJ))) {
                // visitedNodes.get(i) -> random(J)
                //this.tourCost += prob.getDistance(this.tour.get(i), randJ);
                this.tour.add(randJ);

                i += 1;
            }
            else {}
        }

        System.out.println("BACK TO DEPOT!");

        // Back to depot
        this.tour.add(0);
        this.tourCost += prob.getDistance(this.tour.get(this.tour.size() - 1), 0);
    }

}