package server.inner_modules.transmitters.transmitter_types;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.data_structures.TimeStamp;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.*;
import server.inner_modules.transmitters.ParentTransmitter;

import static server.enumerators.TIMESTAMP_NAME.*;

public class StubTransmitter extends ParentTransmitter {

    public StubTransmitter(
        StateController stateController,
        SettingsController settingsController,
        ReadyLists readyLists,
        TransmitionInformationObject transmitionInformationObject,
        MeasurementController measurementController
    ){
        super(stateController,settingsController,readyLists,transmitionInformationObject,measurementController);
    }

    @Override
    public void transmit(IORequest request){
        //TODO: in the future I could add a programmed delay
    }

    @Override
    public void transmit(IORequest[] requests){
    	//TODO: in the future I could add a programmed delay
    }

}
