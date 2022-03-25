package server.inner_modules.transmitters;

import server.data_structures.*;
import server.enumerators.*;
import server.inner_modules.MeasurementController;
import server.inner_modules.*;

import java.util.*;

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
    public void transmit(IORequest[] requests){}
    public void transmit(IORequest request){}

    //Methods implemented by parent
    public void transmit(Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> buffer){

        if(buffer == null){
            return;
        }
        int numRequests = ReadyLists.getNumberOfRequestsInBuffer(buffer);
        if(numRequests == 0){
            return;
        }

        

        //List of requests to be transmitted
        IORequest[] requests = new IORequest[numRequests];

        //variables to loop through the buffer
        Hashtable<Integer,SyncIORequestLinkedList> currBatch;
        SyncIORequestLinkedList currApp;
        Iterator currItr;
        IORequest request;
        int currRequestNumber = 0;
        
        //loop through the buffer and add requests to list of requests to transmit
        Set<Integer> batchIds = buffer.keySet();
        for(Integer batchId:batchIds){
            currBatch = buffer.get(batchId);
            if(currBatch != null){
                Set<Integer> appIds = currBatch.keySet();
                for(Integer appId: appIds){
                    currApp = currBatch.get(appId);
                    if(currApp != null && currApp.getNumberOfRequests()>0){
                        currItr = currApp.iterator();
                        while(currItr.hasNext()) {
                            request = (IORequest)currItr.next();
                            requests[currRequestNumber++] = request;
                        }
                    }
                }
            }
        }

        if(settingsController.getString("dataTransferTechnique").equals("a")){
            for(int reqnum=0;reqnum<numRequests;reqnum++){
                transmit(requests[reqnum]);
            }
        }else if(numRequests>0){
            transmit(requests);
        }
    }

    public void add_transmitter_measurements_to_request_and_measurement_controller(IORequest request, long transmitter_entry_time, long transmitter_exit_time){
        if(request != null){
            request.addTimeStamp(TIMESTAMP_NAME.READY_LIST_EXIT,transmitter_entry_time);
                request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_END,transmitter_entry_time); 
                request.addTimeStamp(TIMESTAMP_NAME.TRANSMITTER_ENTRY,transmitter_entry_time);
                request.addTimeStamp(TIMESTAMP_NAME.TRANSMITTER_EXIT,transmitter_exit_time);
            for(TimeStamp t: request.getTimeStamps()){
                measurementController.addMeasurement(t);
            }
        }
    }

    public void add_transmitter_measurements_to_request_and_measurement_controller(SyncIORequestLinkedList app, long transmitter_entry_time, long transmitter_exit_time){
        Iterator itr = app.iterator();
        IORequest request;
        while(itr.hasNext()) {
            request = (IORequest)itr.next();
            add_transmitter_measurements_to_request_and_measurement_controller(request,transmitter_entry_time,transmitter_exit_time);
        }
    }

    public void add_transmitter_measurements_to_request_and_measurement_controller(Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> buffer, long transmitter_entry_time, long transmitter_exit_time){
        Hashtable<Integer,SyncIORequestLinkedList> currBatch;
        SyncIORequestLinkedList currApp;

        Set<Integer> batchIds = buffer.keySet();
        for(Integer batchId:batchIds){
            currBatch = buffer.get(batchId);
            Set<Integer> appIds = currBatch.keySet();
            for(Integer appId:appIds){
                currApp = currBatch.get(appId);
                add_transmitter_measurements_to_request_and_measurement_controller(currApp,transmitter_entry_time,transmitter_exit_time);
            }
        }
    }

    @Override
    public void run(){
        while(isRunning){
            try{
                if(stateController.isStateRunning() && isRunning){
                    transmitionInformationObject.waitForNotificationToStartTransmition();
                }
                
                //check again because state could change while thread was waiting
                if(stateController.isStateRunning() && isRunning){ //isRunning condition added because thread could have been waiting while isRunning was set to false
                    if(settingsController.getIsVerbose()){
                        System.out.println("Transmitter: Starting data Transfer...");
                    }

                    //Get All requests from buffer. This will change when using techniques E and D
                    Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> requestToTransmit = buffer.removeAllFromBuffer();
                    long transmitter_entry_time = System.nanoTime();

                    int numberOfRequestsToTransmit = 0;
                    if(requestToTransmit != null){
                        numberOfRequestsToTransmit = ReadyLists.getNumberOfBytesInBuffer(requestToTransmit);
                        if(numberOfRequestsToTransmit > 0){
                            if(settingsController.getIsVerbose()){
                                System.out.println("Transmitter: going to transmit " + numberOfRequestsToTransmit + " requests");
                            }

                            //transmit Requests
                            transmit(requestToTransmit);

                            if(settingsController.getIsVerbose()){
                                System.out.println("Transmitter: transmitted " + numberOfRequestsToTransmit + " requests");
                            }

                            //Add transmitter Measurements
                            long transmitter_exit_time = System.nanoTime();
                            add_transmitter_measurements_to_request_and_measurement_controller(requestToTransmit,transmitter_entry_time,transmitter_exit_time);

                            //clear requestsToTransmit
                            ReadyLists.clear(requestToTransmit);

                        }else if(settingsController.getIsVerbose()){
                            System.out.println("Transmitter: 0 requests to transmit");
                        }
                    }else if(settingsController.getIsVerbose()){
                        System.out.println("Transmitter: requests to transmit is null");
                    }
                }else if(isRunning){
                    //State is NOT running. Transfer technique is not allowed to run
                    Thread.sleep(100);
                }
            }catch(Exception e){
                System.out.println("Unnable to perform the data transfer");
                e.printStackTrace();
            }finally{
                if(settingsController.getIsVerbose()){
                    System.out.println("Transmitter: tio end of transmition");
                }
                transmitionInformationObject.endOfTransmition();
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
