package server.inner_modules;

import server.data_structures.*;
import server.enumerators.*;
import server.inner_modules.*;


public class Sorter implements Runnable{
    private SyncIORequestLinkedList entryQueue;
    private ReadyLists buffer;
    private SettingsController settingsController;
    private StateController stateController;
    private boolean isFinished;

    public Sorter(
            SyncIORequestLinkedList entryQueue, 
            ReadyLists buffer,
            SettingsController settingsController,
            StateController stateController
            ){
        this.entryQueue = entryQueue;
        this.buffer = buffer;
        this.settingsController = settingsController;
        this.stateController = stateController;
        this.isFinished = false;
    }


    @Override
    public void run() {
        while(!isFinished){
            try{
                if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
                    IORequest request = entryQueue.take();
                    request.addTimeStamp(TIMESTAMP_NAME.ENTRY_LIST_EXIT); //add entry list exit timestamp with current time
                    request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_START); //add service time start timestamp with current time 
                    
                    if(settingsController.getIsVerbose()){
                        System.out.println("Storer took request " + request.getRequestId() + " from entry list");
                    }

                    //add buffer entry timestamp
                    request.addTimeStamp(TIMESTAMP_NAME.READY_LIST_ENTRY);

                    //add request to ready lists
                    buffer.add(request);
                }else{
                    Thread.sleep(100);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void stop(){
        this.isFinished = true;
    }
}