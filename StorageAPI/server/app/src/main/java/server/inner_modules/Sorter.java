import server.data_structures.*;
import server.enumerators.*;
import server.inner_modules.*;
import server.inner_modules.service_time_creators.ParentServiceTimeCreator;

public class Sorter implements Runnable{
    private SyncIORequestLinkedList entryQueue;
    private ReadyLists buffer;
    private ParentServiceTimeCreator parentServiceTimeCreator;
    private SettingsController settingsController;
    private StateController stateController;
    private boolean isFinished;

    public Sorter(
            SyncIORequestLinkedList entryQueue, 
            ReadyLists buffer,
            ParentServiceTimeCreator parentServiceTimeCreator,
            SettingsController settingsController,
            StateController stateController
            ){
        this.entryQueue = entryQueue;
        this.buffer = buffer;
        this.parentServiceTimeCreator = parentServiceTimeCreator;
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
                    request.addTimeStamp(TIMESTAMP_NAME.ENTRY_LIST_EXIT); //add timestamp with current time to request

                    if(settingsController.getIsVerbose()){
                        System.out.println("Storer took request " + request.getRequestId() + " from entry list");
                    }

                    //Mock Processing of request
                    request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_START);
                    parentServiceTimeCreator.createServiceTime();
                    request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_END);

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