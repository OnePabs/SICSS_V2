package StorageManager;

import StorageManager.data_structures.*;
import StorageManager.executors.*;
import StorageManager.storage_platforms.*;


public class StorageManager implements Runnable {
    private SettingsController settingsController;
    private SyncIORequestLinkedList insertOneEntryQueue;
    private SyncStringLinkedList commitAllEntryQueue;
    private StorageCommitAllExecutor commitAllExecutor;
    private StorageInsertOneExecutor insertOneExecutor;
    private MeasurementController measurementController;

    public StorageManager(
            SettingsController settingsController,
            SyncIORequestLinkedList insertOneEntryQueue,
            SyncStringLinkedList commitAllEntryQueue,
            MeasurementController measurementController
    ){
        this.settingsController  = settingsController;
        this.insertOneEntryQueue = insertOneEntryQueue;
        this.commitAllEntryQueue = commitAllEntryQueue;
        this.measurementController = measurementController;
        this.commitAllExecutor = null;
        this.insertOneExecutor = null;
    }

    @Override
    public void run() {
        while(true){

            try {
                synchronized (settingsController.changaOfSettingsNotifier){
                    settingsController.changaOfSettingsNotifier.wait();
                }

                if(settingsController.getIsVerbose()){
                    System.out.println("Data Transfer Controller: Settings Controller Changed Settings");
                }

                if(commitAllExecutor != null){
                    commitAllExecutor.stop();
                }

                if(insertOneExecutor != null){
                    insertOneExecutor.stop();
                }

                //Todo: change this to include settings from settings controller
                //create platform
                ParentStoragePlatform platform;
                String platformName = settingsController.getString("platform");
                switch(platformName){
                    case "mysql":
                        platform = new MysqlApi();
                        break;
                    case "stub":
                        platform = new StubPlatform(settingsController);
                        break;
                    default:
                        platform = new ParentStoragePlatform();
                }


                //clear entry queues
                insertOneEntryQueue.clear();
                commitAllEntryQueue.clear();

                //create executors
                insertOneExecutor = new StorageInsertOneExecutor(insertOneEntryQueue,platform,measurementController);
                commitAllExecutor = new StorageCommitAllExecutor(commitAllEntryQueue,platform,measurementController);

                //run executors
                new Thread(insertOneExecutor).start();
                new Thread(commitAllExecutor).start();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}