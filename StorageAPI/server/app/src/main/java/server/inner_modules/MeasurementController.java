package server.inner_modules;

import server.data_structures.TimeStamp;
import server.enumerators.PROGRAM_STATE;
import java.util.LinkedList;

public class MeasurementController {
    private StateController stateController;
    private LinkedList<TimeStamp> measurements;

    public MeasurementController(StateController stateController){
        this.stateController = stateController;
        measurements = new LinkedList<TimeStamp>();
    }

    public boolean addMeasurement(TimeStamp tp){
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
            measurements.add(tp);
        }
        return false;
    }

    public TimeStamp[] getMeasurements(){
        System.out.println("Measurement controller num measurements: " + measurements.size());
        return measurements.toArray(new TimeStamp[measurements.size()]);
    }

    public void clear(){
        measurements.clear();
    }
}
