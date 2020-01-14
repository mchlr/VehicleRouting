package solver;

import java.util.Comparator;

public class TourLengthComparator implements Comparator<Ant> {
    private static TourLengthComparator me; 


    private TourLengthComparator() {
        me = this;
    }

    public static TourLengthComparator getInstance() {
        if(me == null) {
            new TourLengthComparator();
        }
        return me;
    }

    @Override
    public int compare(Ant o1, Ant o2) {
        return (int)(o1.getTourCost() - o2.getTourCost());
    }

}