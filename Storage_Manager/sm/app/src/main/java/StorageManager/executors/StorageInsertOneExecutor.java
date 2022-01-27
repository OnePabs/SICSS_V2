package StorageManager.executors;

import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;

public class StorageInsertOneExecutor implements Runnable{
    private boolean isRunning;
    private SyncIORequestLinkedList entryQueue;
    private ParentStoragePlatform platform;

    public StorageInsertOneExecutor(SyncIORequestLinkedList entryQueue, ParentStoragePlatform platform){
        this.entryQueue = entryQueue;
        this.platform = platform;
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                IORequest request = entryQueue.take();
                platform.insertOne(request.getContent());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

}