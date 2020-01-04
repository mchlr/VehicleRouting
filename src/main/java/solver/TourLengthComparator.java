package solver;

import java.util.Comparator;

public class TourLengthComparator implements Comparator<Ant> {

    @Override
    public int compare(Ant o1, Ant o2) {
        return (int)(o1.getTourCost() - o2.getTourCost());
    }

}