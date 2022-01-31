package common;

import java.util.Comparator;

public class MeasurementsComparator implements Comparator<MeasurementEntry> {
    public int compare(MeasurementEntry e1, MeasurementEntry e2) {
        int longComparisonResult = e1.id - e2.id;
        int intComparisonResult;
        if(longComparisonResult > 0) {
            intComparisonResult = 1;
        }else if(longComparisonResult == 0) {
            intComparisonResult = 0;
        }else {
            intComparisonResult = -1;
        }
        return intComparisonResult;
    }
}
