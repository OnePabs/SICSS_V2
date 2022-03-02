package server.inner_modules.transmitters;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.MeasurementController;
import server.inner_modules.*;

public class ParentTransmitter implements Runnable{
    protected StateController stateController;
    protected SettingsController settingsController;
    protected ReadyLists buffer;
    protected TransmitionInformationObject transmitionInformationObject;
    protected MeasurementController measurementController;
    private boolean isRunning;

    //constructor
    public ParentTransmitter(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject,
            MeasurementController measurementController
    ){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.buffer = buffer;
        this.transmitionInformationObject = transmitionInformationObject;
        this.measurementController = measurementController;
        this.isRunning = true;
    }

    //METHODS TO OVERRIDE
    public void transmit(IORequest request){}
    public void transmit(SyncIORequestLinkedList requestList){}

    public void addMeasurementsToMeasurementController(IORequest request){}

    public void addMeasurementsToMeasurementController(SyncIORequestLinkedList requestList){}

    @Override
    public void run(){
        while(isRunning){
            try{
                if(stateController.isStateRunning() && isRunning){
                    synchronized(transmitionInformationObject.transmitionNotifier){
                        transmitionInformationObject.transmitionNotifier.wait();
                    }
                }
                
                //check again because state could change while thread was waiting
                if(stateController.isStateRunning() && isRunning){ //isRunning condition added because thread could have been waiting while isRunning was set to false
                    if(settingsController.getIsVerbose()){
                        System.out.println("Transmitter: Starting data Transfer...");
                    }
                    
                    SyncIORequestLinkedList requestToTransmit = buffer.getAndRemoveFromAllBatches();
                    if(requestToTransmit == null){
                        System.out.println("Transmitter: requestToTransmit is null");
                    }else{
                        try{
                            int numRequestToTransmit = requestToTransmit.getNumberOfRequests();
                            if(settingsController.getIsVerbose()){
                                    System.out.println("Transmitter: " + numRequestToTransmit + " were passed to transmit");
                            }

                            if(numRequestToTransmit == 1){
                                IORequest request = requestToTransmit.take();
                                this.transmit(request);
                            }else if(numRequestToTransmit > 1){
                                this.transmit(requestToTransmit);
                            }else{
                                if(settingsController.getIsVerbose()){
                                    System.out.println("Transmitter: No requests to transmit. Transmition aborted");
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else if(isRunning){
                    //State is NOT running. Transfer technique is not allowed to run
                    Thread.sleep(100);
                }
            }catch(Exception e){
                System.out.println("Unnable to perform the data transfer");
                e.printStackTrace();
            }
        }

        if(settingsController.getIsVerbose()){
            System.out.println("Transmitter: Finishing execution");
        }
    }

    public void finishExecution(){
        isRunning = false;
    }
}
