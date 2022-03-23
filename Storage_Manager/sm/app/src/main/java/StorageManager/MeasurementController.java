package StorageManager;

import java.util.LinkedList;

public class MeasurementController {
    private LinkedList<TimeStamp> measurements;
    private StateController stateController;

    public MeasurementController(StateController stateController){
        this.stateController = stateController;
        measurements = new LinkedList<TimeStamp>();
    }

    public synchronized boolean addMeasurement(TimeStamp tp){
        if(stateController.isStateRunning()){
            measurements.add(tp);
            return true;
        }else{
            return false;
        }
        
    }

    public synchronized TimeStamp[] getMeasurements(){
        if(!stateController.isStateRunning()){
            return measurements.toArray(new TimeStamp[measurements.size()]);
        }else{
            return null;
        }
    } 

    public synchronized void clear(){
        measurements.clear();
    }
}
