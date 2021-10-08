package server.inner_modules;


import server.data_structures.MeasurementList;
import server.enumerators.PROGRAM_STATE;

import java.util.Hashtable;
import java.util.LinkedList;

public class MeasurementController {
    private StateController stateController;
    private Hashtable<String, MeasurementList> measurementListHashtable;

    public MeasurementController(){
        this.measurementListHashtable = new Hashtable<String, MeasurementList>();
    }

    public void setStateController(StateController stateController){
        this.stateController = stateController;
    }

    public boolean addMeasurement(String measurementName, Long measurementValue){
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
            MeasurementList ml;
            if(measurementListHashtable.containsKey(measurementName)){
                ml = measurementListHashtable.get(measurementName);
                ml.addMeasurement(measurementValue);
            }else{
                ml = new MeasurementList(measurementName);
                ml.addMeasurement(measurementValue);
                measurementListHashtable.put(measurementName,ml);
            }
            return true;
        }
        return false;
    }

    public boolean clearMeasurentList(String measurementName){
        if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED){
            if(measurementListHashtable.containsKey(measurementName)){
                MeasurementList ml = measurementListHashtable.get(measurementName);
                ml.clearMeasurements();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean clearAllMeasurements(){
        if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED) {
            this.measurementListHashtable = new Hashtable<String, MeasurementList>();
            return true;
        }
        return false;
    }

    public Long[] getAllMeasurementsOf(String measurementName){
        if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED) {
            if (this.measurementListHashtable.containsKey(measurementName)) {
                MeasurementList ml = measurementListHashtable.get(measurementName);
                return ml.getMeasurements();
            }
        }
        return null;
    }
}
