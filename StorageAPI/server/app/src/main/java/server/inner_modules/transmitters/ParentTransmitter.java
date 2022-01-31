package server.inner_modules.transmitters;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

public class ParentTransmitter {
    protected StateController stateController;
    protected SettingsController settingsController;
    protected ReadyLists readyLists;
    protected MeasurementController measurementController;

    //constructor
    public ParentTransmitter(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists readyLists,
            MeasurementController measurementController
    ){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.readyLists = readyLists;
        this.measurementController = measurementController;
    }

    //METHODS TO OVERRIDE
    public void transmit(IORequest request){}
    public void transmit(SyncIORequestLinkedList requestList){}

    public void addMeasurementsToMeasurementController(IORequest request){

    }

    public void addMeasurementsToMeasurementController(SyncIORequestLinkedList requestList){
        //todo
    }
}
