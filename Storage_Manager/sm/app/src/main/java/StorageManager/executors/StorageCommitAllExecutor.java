package StorageManager.executors;

import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;
import StorageManager.*;

public class StorageCommitAllExecutor implements Runnable{
    private boolean isRunning;
    private SyncStringLinkedList entryQueue;
    private ParentStoragePlatform platform;
    private MeasurementController measurementController;

    public StorageCommitAllExecutor(SyncStringLinkedList entryQueue, ParentStoragePlatform platform, MeasurementController measurementController){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                String requests = entryQueue.take();
                measurementController.addMeasurement(new TimeStamp(0,"QueueExitTime",System.nanoTime()));
                platform.commitAll(requests);
                measurementController.addMeasurement(new TimeStamp(0,"EXIT",System.nanoTime()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

}