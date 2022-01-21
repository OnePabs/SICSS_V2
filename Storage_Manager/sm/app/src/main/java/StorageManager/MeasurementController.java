package StorageManager;

import java.util.LinkedList;

public class MeasurementController {
    private LinkedList<TimeStamp> measurements;

    public MeasurementController(){
        measurements = new LinkedList<TimeStamp>();
    }

    public boolean addMeasurement(TimeStamp tp){
        measurements.add(tp);
        return true;
    }

    public TimeStamp[] getMeasurements(){
        return measurements.toArray(new TimeStamp[measurements.size()]);
    }

    public void clear(){
        measurements.clear();
    }
}
