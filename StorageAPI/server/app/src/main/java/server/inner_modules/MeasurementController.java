package server.inner_modules;

import server.data_structures.MeasurementList;
import server.data_structures.TimeStamp;
import server.enumerators.PROGRAM_STATE;

import java.util.Hashtable;
import java.util.LinkedList;

public class MeasurementController {
    private StateController stateController;
    private LinkedList<TimeStamp> measurements;

    public MeasurementController(StateController stateController){
        this.stateController = stateController;
    }

    public boolean addMeasurement(TimeStamp tp){
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
            measurements.add(tp);
        }
        return false;
    }
}
