package solver;

import java.util.*;



import model.CVRPProblemInstance;

public class ACOSolver {

    CVRPProblemInstance ref;
    private double[][] phero;
    private double [][] probMat;
    private double alpha ;
    private double beta;
    private double gamma;
    private double roh;
    private double theta;
    private List<Ant> antInstances; 


    public ACOSolver(CVRPProblemInstance ref, int antAmount, double alpha, double beta, double gamma, double roh, double theta) {
        this.ref = ref;
        this.antInstances = new ArrayList<>();
        this.alpha =alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.probMat = null;
        this.roh = roh;
        this.theta = theta;
        initializePheroMatrix();
        initailizeAnts(antAmount, probMat);
    }

    public void solve() {
        int antIdx = 0;
        for(Ant nxt : antInstances) {
            nxt.sovleProblem(this.ref);
            System.out.println("> Ant #" + antIdx + " generated a Route.");

            antIdx++;
        }

        // Report Ant stats;
        for(int i = 0; i < antInstances.size(); i++) {
            System.out.println("Ant#" + i + " - Length: " + antInstances.get(i).getTourCost());
        }

        // TODO: Add heuristics here;


        // Update the pheromone-matrix
        System.out.println("PRE-UPDATE PheroMatrix:");
        // System.out.println(Arrays.deepToString(this.phero));
        prettyprintmatrix(this.phero);

        // TODO: Add function which gets the top-n Ants (top in regard to tourCost);
        // Just pass all the ants into the pheroDeposition

        pheroVaporated();
        System.out.println("POST-UPDATE PheroMatrix:");
        System.out.println("Vaporisation");
        System.out.println("\n");
       // System.out.println(Arrays.deepToString(this.phero));
        prettyprintmatrix(this.phero);
        System.out.println("\n");
        pheroDeposition(antInstances);
        System.out.println("POST-UPDATE PheroMatrix:");
        System.out.println("Deposition");
        System.out.println("\n");
     //   System.out.println(Arrays.deepToString(this.phero));
        prettyprintmatrix(this.phero);

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

    // Calc Lavg for Vaporated
    private double Lavg(List<Ant> sweetants){
        double lavg = 0;

        for( Ant antworker : sweetants){

            lavg += antworker.getTourCost() / sweetants.size();
        }
        return lavg;
    }

    // Update PheroMatrix with Vaporated Method
    private void pheroVaporated () {

        for (int i = 0; i < phero.length; i++)
            for (int j = 0; j < phero.length; j++) {
                phero[i][j] = (roh + (theta / Lavg(antInstances))) * phero[i][j];
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

    private void initailizeAnts(int n, double [][] probMat) {
        for(int i = 0; i < n; i++) {
            this.antInstances.add(new Ant(probMat, ref.capacity));
        }
    }


    private void calcProbs() {
        double sumProbDis = 0;


        for (int i = 0; i < ref.getDimensions(); i++) {
            for (int j = 0; j < ref.getDimensions(); j++) {
                sumProbDis += Math.pow(phero[i][j], alpha) * Math.pow(1 / ref.getDistance(i, j), beta) * Math.pow(ref.getDistance(i, 0) + ref.getDistance(0, j) - ref.getDistance(i, j), gamma);

            }
        }

        for (int i = 0; i < ref.getDimensions(); i++) {
            for (int j = 0; j < ref.getDimensions(); j++) {
                probMat[i][j] = (Math.pow(phero[i][j], alpha) * Math.pow(1 / ref.getDistance(i, j), beta) * Math.pow(ref.getDistance(i, 0) + ref.getDistance(0, j) - ref.getDistance(i, j), gamma)) / (sumProbDis);
            }
        }

    }
       private void prettyprintmatrix (double[][] Matrix){
                Arrays.stream(Matrix).forEach(
                                (row) -> {
                                    System.out.print("[");
                                    Arrays.stream(row).forEach((el) -> System.out.print(" " + el + " "));
                                    System.out.println("]");
                                }
                        );
            }



    }


