package solver;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import heuristics.HeuristicsHelper;
import util.*;

import model.CVRPProblemInstance;

public class ACOSolver {

    CVRPProblemInstance ref;
    private double[][] phero;
    private double[][] probMat;
    private int iterCount;
    private int antCount;
    private int topAntCount;
    private double alpha;
    private double beta;
    private double gamma;
    private double roh;
    private double theta;
    private int noBetterFor;
    private List<Ant> antInstances;

    public ACOSolver(CVRPProblemInstance ref, int iterCount, int betterCount, int antAmount, int topAntCount, double pheroValue, double alpha, double beta, double gamma, double roh,
            double theta) {
        this.ref = ref;
        this.iterCount = iterCount;
        this.antInstances = new ArrayList<>();
        this.topAntCount = topAntCount;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.probMat = null;
        this.roh = roh;
        this.theta = theta;
        this.noBetterFor = betterCount;

        this.antCount = antAmount;
        initializePheroMatrix(pheroValue);

        calcProbs();

        initailizeAnts(this.ref, this.antCount, probMat);
    }

    public void solve() {
        int iterCount = 0;

        double minCost = Double.MAX_VALUE;
        Integer minIter = -1;

        Date startTime = Calendar.getInstance().getTime();

        FileHelper myFileHelper = new FileHelper(ref.name);
        HeuristicsHelper.setProblemReference(ref);
        ExecutorService antExecutor = null;

        System.out.println("Now solving: " + ref.name + " with AntColonyOptimization!");


        while (iterCount < this.iterCount) {
            System.out.println("\n");
            
            antExecutor = Executors.newFixedThreadPool(antInstances.size());
            double meanLength = 0;

            // Add Ants into the ExecutorService;
            List<Future<Ant>> threadAnts = new ArrayList<>();
            for(Ant a : antInstances) {
                threadAnts.add(antExecutor.submit(a));
            }
    
            // Run Ants using multiple threads;
            try {
                antExecutor.shutdown();
    
                if(antExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
    
                    List<Ant> ret = new ArrayList<>();
                    for(int i = 0; i < threadAnts.size(); i++) {
                        Ant tmp = threadAnts.get(i).get();
                        meanLength += tmp.getTourCost();
                        ret.add(tmp);
                    }
                    antInstances = ret;
                }
            }
            catch(Exception ex) {
                System.err.println("Error while multithreading Ants! :(");
                System.err.println(ex);
            }

            double avgCost = (meanLength/antInstances.size());
            System.out.println("> Iteration #" + iterCount +  " - Mean tour cost = " + avgCost);

            // Sort Ants by their tour length;
            antInstances.sort(TourLengthComparator.getInstance());

            // Evaporate pheromons;
            pheroVaporated();

            // Use the amount of topAnts in order to deposite pheromons;
            pheroDeposition(antInstances.subList(0, topAntCount));

            // Store the current tour into the file helper for later visualization;
            myFileHelper.storeTour(antInstances, avgCost, this.phero, iterCount);

            // Update the probability matrix, that is being used in the next generation of Ants;
            calcProbs();

            // Check the tour-length of the best ant.
            // If there is a new smallest-tour-ever => Update minCost to the cost of this tour & minIter to the generation number where this tour was found;
            if(antInstances.get(0).getTourCost() < minCost){
                minCost = antInstances.get(0).getTourCost();
                minIter = iterCount;
            }


            if( (iterCount - minIter >= noBetterFor)) {
                System.out.println("No improved solution since generation " + minIter + ".\n(Done additional " + noBetterFor + " iterations)");
                break;
            }
            if((iterCount == this.iterCount-1) ) {
                System.out.println("Max Iteration reached!\nTerminating Algorithm...");
                break;
            }

            // If it's not the last run: Re-Initialize Ants for the next run;
            this.antInstances = new ArrayList<>();
            initailizeAnts(this.ref, this.antCount, probMat);

            iterCount++;
        }

        System.out.println("Optimization finished!");
        System.out.println("\n");

        System.out.println("Top-Ants from best generation");

        int i = 1;
        Map<List<List<Integer>>, List<Double>> bestGen = myFileHelper.getTour(minIter);
        for(List<List<Integer>> tour : bestGen.keySet()) {
            System.out.println("#" + i + " => " +  bestGen.get(tour));
        }
        System.out.println("\n");

        System.out.println("Writing Tours to disk...");
        myFileHelper.writeStoredToursToFile();
        System.out.println("Done! \n");

        Date endTime = Calendar.getInstance().getTime();
        System.out.println("Started ACO  at: " + startTime);
        System.out.println("Finished ACO at: " + endTime);
    }

    // #region Initialization Methods

    // Methods for initializing this class (Called within constructor);

    private void initializePheroMatrix(double initalPhero) {
        this.phero = new double[this.ref.getDimensions()][this.ref.getDimensions()];

        // Initalize the Pheromone Matrix with all ones;
        for (int i = 0; i < this.phero.length; i++) {
            for (int j = 0; j < this.phero.length; j++) {
                this.phero[i][j] = i==j ? 0.0 : initalPhero;
            }
        }
    }

    private void initailizeAnts(CVRPProblemInstance ref, int n, double[][] probMat) {
        for (int i = 0; i < n; i++) {
            this.antInstances.add(new Ant(probMat, ref.capacity, ref));
        }
    }

    // #endregion

    // #region Pheromon Updates

    private void pheroDeposition(List<Ant> topAnts) {

        int sig = topAnts.size();
        int lamb = 1;

        List<Integer> bestTour = null;
        double bestTourCost = Double.MAX_VALUE;
        for (Ant nxt : topAnts) {
            if (nxt.getTourCost() < bestTourCost) {
                bestTour = nxt.getTour();
                bestTourCost = nxt.getTourCost();
            }
        }

        for (Ant currAnt : topAnts) {         
            currAnt.setTour(Arrays.asList(HeuristicsHelper.twoOpt(currAnt.getTour().toArray(Integer[]::new))));
            currAnt.setTour(Arrays.asList(HeuristicsHelper.Swap(currAnt.getTour().toArray(Integer[]::new))));

            List<Integer> antTour = currAnt.getTour();


            for (int i = 0; i < antTour.size() - 1; i++) {
                double sigLamb = /* this.phero[antTour.get(i)][antTour.get(i+1)] + */ (sig - lamb)
                        / currAnt.getTourCost();

                double sigStar = containsArc(bestTour, new int[] { antTour.get(i), antTour.get(i + 1) })
                        ? (sig / bestTourCost)
                        : 0;

                this.phero[antTour.get(i)][antTour.get(i + 1)] = this.phero[antTour.get(i)][antTour.get(i + 1)]
                        + sigLamb + sigStar;
            }
            lamb += 1;
        }

        // Add normalization here;

    }

    // Update PheroMatrix with Vaporated Method
    private void pheroVaporated() {
        for (int i = 0; i < phero.length; i++) {
            for (int j = 0; j < phero.length; j++) {
                phero[i][j] = (roh + (theta / Lavg(antInstances))) * phero[i][j];
            }
        }
    }

    // #endregion

    // Calc Lavg for Vaporated
    private double Lavg(List<Ant> sweetants) {
        double lavg = 0;

        for (Ant antworker : sweetants) {

            lavg += antworker.getTourCost() / sweetants.size();
        }
        return lavg;
    }

    private boolean containsArc(List<Integer> bestTour, int[] targetArc) {
        for (int y = 0; y < bestTour.size() - 1; y++) {
            if (bestTour.get(y) == targetArc[0] && bestTour.get(y + 1) == targetArc[1]) {
                return true;
            }
        }
        return false;
    }

    private void calcProbs() {

        if (probMat == null) {
            // Modified Dimensions for ignoring the depot
            // Therefore, use dimensions -1
            // This implies, that the idx's in the array have to be calculated like (idx+1)
            // in order to get the correct node
            int modDim = ref.getDimensions();

            probMat = new double[modDim][modDim];

            for (int i = 0; i < modDim; i++) {
                for (int j = 0; j < modDim; j++) {
                    var koelnCalc = i == j ? 0 : (1.0 / (modDim - 1));
                    probMat[i][j] = koelnCalc;
                }
            }
        } else {
            double sumProbDis = 0;
            for (int i = 0; i < ref.getDimensions(); i++) {
                for (int j = 0; j < ref.getDimensions(); j++) {
                    sumProbDis += Math.pow(phero[i][j], alpha)
                            * (i == j ? 1 : Math.pow(1 / ref.getDistance(i, j), beta))
                            * Math.pow(ref.getDistance(i, 0) + ref.getDistance(0, j) - ref.getDistance(i, j), gamma);

                }
            }

            for (int i = 0; i < ref.getDimensions(); i++) {
                for (int j = 0; j < ref.getDimensions(); j++) {

                    var a =  (Math.pow(phero[i][j], alpha));
                    var b = (i == j ? 1 : Math.pow(1 / ref.getDistance(i, j), beta));

                    var c_One = -1337.0;

                    if(i == 0) {
                        c_One = ref.getDistance(i, j);
                    }
                    else {
                        c_One = ref.getDistance(i, 0) + ref.getDistance(0, j) - ref.getDistance(i, j);
                    }
                    var c = Math.pow(c_One, gamma);

                    probMat[i][j] = (a * b * c) / (sumProbDis);
                }
            }
        }
    }
}