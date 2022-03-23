package StorageManager.executors;

import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;
import StorageManager.*;

public class StorageInsertOneExecutor implements Runnable{
    private boolean isRunning;
    private SyncIORequestLinkedList entryQueue;
    private ParentStoragePlatform platform;
    private StateController stateController;
    private MeasurementController measurementController;

    public StorageInsertOneExecutor(
        SyncIORequestLinkedList entryQueue, 
        ParentStoragePlatform platform, 
        StateController stateController,
        MeasurementController measurementController){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.stateController = stateController;
        this.measurementController = measurementController;
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                if(stateController.isStateRunning()){
                    IORequest request = entryQueue.take();
                    if(request!=null){
                        measurementController.addMeasurement(new TimeStamp(request.getRequestId(),"QueueExitTime",System.nanoTime()));
                        platform.insertOne(request.getContent());
                        measurementController.addMeasurement(new TimeStamp(request.getRequestId(),"EXIT",System.nanoTime()));
                    }else{
                        try{
                            Thread.sleep(200);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    try{
                        Thread.sleep(200);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

}