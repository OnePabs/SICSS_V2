package server.inner_modules.transmitters;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.MeasurementController;
import server.inner_modules.StateController;

public class ParentTransmitter {
    private StateController stateController;
    private ReadyLists readyLists;
    protected MeasurementController measurementController;

    //constructor
    public ParentTransmitter(){}

    //setters
    public void setStateController(StateController stateController) {
        this.stateController = stateController;
    }
    public void setReadyLists(ReadyLists readyLists) {
        this.readyLists = readyLists;
    }
    public void setMeasurementController(MeasurementController measurementController) {
        this.measurementController = measurementController;
    }

    //METHODS TO OVERRIDE
    public void transmit(IORequest request){}
    public void transmit(SyncIORequestLinkedList requestList){}
}
