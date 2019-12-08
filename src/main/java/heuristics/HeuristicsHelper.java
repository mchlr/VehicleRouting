package heuristics;

import java.util.*;

public class HeuristicsHelper {

    public static int[] threeOpt(Integer[] tour) {
        System.out.println("[3opt] - Got tour: " + Arrays.toString(tour));

        // 3opt since it tries to create 3 SUBTOURS!!!
        // if we have 9 nodes in our tour -> 3 Subtours with 3 nodes each;

        // TODO: Check what happens if the tour isn't devideable by 3!
        int three_split = (int)tour.length % 3; 


        List<Integer> changes = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            int changeIdx = (int)(Math.random() * tour.length);
            changes.add(changeIdx);
            tour[changeIdx] = null;
        }
        System.out.println("[3opt] - Now changing: " + changes.toString());

        for(Integer change : changes) {

        }

        return new int[0];
    }
}
