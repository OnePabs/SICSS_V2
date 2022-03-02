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
        if(stateController.getCurrentState()== PROGRAM_STATE.RUNNING){
            request.addTimeStamp(TRANSMITTER_ENTRY);
            for(TimeStamp t: request.getTimeStamps()){
                measurementController.addMeasurement(t);
            }
            request.addTimeStamp(TRANSMITTER_EXIT);
            if(settingsController.getIsVerbose()){
                System.out.println("Stub Transmitter: IORequest " + request.getRequestId() + " leaving storage  API");
            }
            measurementController.addMeasurement(request.getTimeStamp(TRANSMITTER_EXIT));
        }
    }

    @Override
    public void transmit(SyncIORequestLinkedList requests){
    	if(settingsController.getIsVerbose()){
                System.out.println("Transmitting SyncIORequestLinkedList");
        }
        if(stateController.getCurrentState()== PROGRAM_STATE.RUNNING){
            int numRequests = requests.getSize();
            for(int i=0;i<numRequests;i++){
                try{
                    IORequest request = requests.take();
                    transmit(request);
                }catch (Exception e){
                    if(settingsController.getIsVerbose()){
                        System.out.println("Problem in stub transmitter. Cannot transmit request");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
