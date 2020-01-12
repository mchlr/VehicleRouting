package heuristics;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Collections;

import model.CVRPProblemInstance;
import util.MathHelper;

public class HeuristicsHelper {

    public static CVRPProblemInstance probReference = null;

    public static void setProblemReference(CVRPProblemInstance ref) {
        probReference = ref;
    }

    // Remove all 0 from the original TOur
    // Pick n-Indices(=> Edges) that will be changed;
    // Remove the nodes at these indices;
    // Shuffle them back into the tour (maybe with all possibilities)
    // Run the tours and add depot-visits if necessary;
    // Check the tour length
    // => Shorter? -> Take that;
    // => Longer? -> Ignore that;

    // Create the change template (Route without depot visits -> aka BigTour);
    // Integer[] changeTemplate = Arrays.asList(ogTour).stream().filter(x -> x >
    // 0).collect(Collectors.toList()).toArray(ogTour);
    public static Integer[] nOpt(Integer[] ogTour, Integer changeCount) {

        if (probReference == null) {
            return null;
        }

        // Calculate the cost of the original (unchanged) tour as a means of comparing
        // the newly generated tours;
        Double originalTourCost = calculateTourCost(ogTour);
        
        // Remove all depot-stops from the given tour;
        List<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < ogTour.length; i++) {
            if (ogTour[i] > 0) {
                tmp.add(ogTour[i]);
            }
        }
        Integer[] changeTemplate = tmp.toArray(new Integer[tmp.size()]);

        // Randomly select edges that will get changed;
        List<Integer> changes = new ArrayList<>();
        for (int i = 0; i < changeCount;) {
            int changeIdx = (int) (Math.random() * changeTemplate.length);
            if(changeTemplate[changeIdx] != null) {
                changes.add(changeTemplate[changeIdx]);
                changeTemplate[changeIdx] = null;
                i++;
            }
        }

        // Test new Permutation
        var myMath = new MathHelper();
        myMath.generatePermutations(changes, 0, changes.size() - 1);

        // Compute all permutations from the removed (changed) edges;
        List<List<Integer>> permChanges = myMath.getPermutations();
        List<Integer[]> permTours = new ArrayList<>();

        // Iterate over all generated permutations;
        for (List<Integer> permut : permChanges) {

            // Create a new tour from the changeTemplate (=> The changed edges are == null in changeTemplate);
            Integer[] newTour = changeTemplate.clone();
            // Iterate over each value within the current permutation;
            for (int j = 0; j < permut.size();) {
                // Find the null-entries and replace null with a new vertex;
                for (int i = 0; i < newTour.length; i++) {
                    if (newTour[i] == null) {
                        newTour[i] = permut.get(j);
                        j++;
                    }
                    // Early brake if all null-entries have already been replaced;
                    if(j >= permut.size()){
                        break;
                    }
                }
            }

            // Add depot visits to newTour;
            newTour = addDepots(newTour);

            // Store the tour;
            permTours.add(newTour);
        }

        // Determine, which of the permutated tours is the best one;
        Double minCost = Double.MAX_VALUE;
        Integer minIdx = 0;
        for(int i = 0; i < permTours.size(); i++) {
            Double currentCost = calculateTourCost(permTours.get(i));
            if(currentCost < minCost) {
                minCost = currentCost;
                minIdx = i;
            }
        }

        // Log-Stuff for debugging;
        // System.out.println("Length (Original): " + originalTourCost);
        // System.out.println("Length (Min-Perm): " + minCost);



        // Check if the originalTour, or the permutated one is shorter and return the shorter one;
        return originalTourCost < minCost ? ogTour : permTours.get(minIdx);
    }

    // Informations for 3-opt Heuristic
    // 3opt since it tries to create 3 SUBTOURS!!!
    // if we have 9 nodes in our tour -> 3 Subtours with 3 nodes each;

    // 3-opt just removes three (random?) edges from the tour and tries to reconnect
    // them;

    private static Double calculateTourCost(Integer[] tour) {
        Double ret = 0.0;
        for (int i = 0; i < tour.length - 1; i++) {
            ret += probReference.getDistance(tour[i], tour[i + 1]);
        }
        return ret;
    }

    private static Integer[] addDepots(Integer[] tour) {
        List<Integer> work = new ArrayList<>();
        int cap = probReference.capacity;
        int load = 0;

        // Depot as startingpoint;
        work.add(0);
        for(int i = 0; i < tour.length; i++) {
            if((load+probReference.getDemand(tour[i])) < cap){
                work.add(tour[i]);
                load += probReference.getDemand(tour[i]);
            }
            else {
                work.add(0);
                work.add(tour[i]);
                load = probReference.getDemand(tour[i]);
            }
        }

        // Depot as finish;
        work.add(0);

        return work.toArray(new Integer[tour.length]);
    }





    private static boolean checkCapacity(Integer [] tour){
        int subtourCap=0;
        int k=0;
        boolean check = false;
        for(int i = 0; i < tour.length-1; i++ ){
           // System.out.println(tour.length);
            if(subtourCap<=probReference.capacity){
                check =true;
            }else{
                return false;
            }

            if(tour[i]==0) {
                subtourCap = 0;
                k++;
            }else{
               // System.out.println("CheckCap counter: "+i);
              subtourCap +=  probReference.getDemand(i-k);
            }

        }
        return check;
    }



    public static /*Integer []*/void Swap(Integer[] tour1, Integer[] tour2){

        ArrayList<Integer> swapped1 = new ArrayList<Integer>();
        ArrayList<Integer> swapped2 = new ArrayList<Integer>();

        Integer[] bestTour = tour1;
        Integer[] newTour = new Integer[tour1.length];
        boolean check=false;
        int i =0 ;


        for(int u = 0; u < tour1.length-1; u++){
            swapped1.add(u);
            swapped2.add(u);
        }
       // System.out.println(swapped1);
      //  System.out.println(swapped2);
      Collections.shuffle(swapped1);
        System.out.println(swapped1);
      Collections.shuffle(swapped2);
        System.out.println(swapped2);

        for(int k=0; i<swapped1.size()-1; i++){

            if(tour1[swapped1.get(k)]==0) {
                tour1[k] = 0;
            }else{
                System.out.println("Bevor Swap: ");
                System.out.println(calculateTourCost(tour1));

                tour1[swapped1.get(k)] = tour2[swapped2.get(k)];


                System.out.println("Nach Swap: ");
                System.out.println(calculateTourCost(tour1));

                System.out.println(checkCapacity(tour1));

                check = (calculateTourCost(tour1)<calculateTourCost(bestTour)) && checkCapacity(tour1);


                if(/*(calculateTourCost(tour1)<calculateTourCost(bestTour) ) && checkCapacity(tour1)*/check == true){
                    System.out.println("Bessere Tour gefunden");
                 /*   return tour1;*/
                }
            }
        }
        //System.out.println("Nichts ist passiert");
       /* return bestTour; */

    }



}
