package util;

import java.util.*;

public class MathHelper {

    private List<List<Integer>> perms = new ArrayList<>();

    public static Double[] normalizeColumn(Double[] col) {

        List<Double> mod = Arrays.asList(col);

        for(int i = 0; i < col.length; i++) {
            // Use rescaling and then round the number;
            col[i] = (col[i] - Collections.min(mod)) / (Collections.max(mod) - Collections.min(mod));
        }

        return col;
    }

    public void generatePermutations(List<Integer> original, Integer start, Integer end) {
        if (start == end) {
            List<Integer> newList = new ArrayList<>();
            for(Integer it : original) {
                Integer itL = it;
                newList.add(itL);
            }
            perms.add(newList); 
        }
        else { 
            for (int i = start; i <= end; i++) { 
                original = swap(original, start, i); 
                generatePermutations(original, start + 1, end); 
                original = swap(original, start, i); 
            } 
        }
    }
    
    public List<Integer> swap(List<Integer> tar, int a, int b) {
        var tmp = tar.get(a);
        tar.set(a, tar.get(b));
        tar.set(b, tmp);

        return tar;
    }

    public List<List<Integer>> getPermutations() {
        return this.perms;
    }
    
}