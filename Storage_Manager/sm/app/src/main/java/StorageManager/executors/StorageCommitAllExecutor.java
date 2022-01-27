package StorageManager.executors;

import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;

public class StorageCommitAllExecutor implements Runnable{
    private boolean isRunning;
    private SyncStringLinkedList entryQueue;
    private ParentStoragePlatform platform;

    public StorageCommitAllExecutor(SyncStringLinkedList entryQueue, ParentStoragePlatform platform){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                String requests = entryQueue.take();
                platform.commitAll(requests);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

}