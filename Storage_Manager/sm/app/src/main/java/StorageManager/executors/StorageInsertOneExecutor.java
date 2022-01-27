package StorageManager.executors;

import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;
import StorageManager.*;

public class StorageInsertOneExecutor implements Runnable{
    private boolean isRunning;
    private SyncIORequestLinkedList entryQueue;
    private ParentStoragePlatform platform;
    private MeasurementController measurementController;

    public StorageInsertOneExecutor(SyncIORequestLinkedList entryQueue, ParentStoragePlatform platform, MeasurementController measurementController){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.measurementController = measurementController;
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                IORequest request = entryQueue.take();
                measurementController.addMeasurement(new TimeStamp(request.getRequestId(),"QueueExitTime",System.nanoTime()));
                platform.insertOne(request.getContent());
                measurementController.addMeasurement(new TimeStamp(request.getRequestId(),"EXIT",System.nanoTime()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

}