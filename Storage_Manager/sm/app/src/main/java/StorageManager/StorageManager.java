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
    private StateController stateController;
    private MeasurementController measurementController;

    public StorageManager(
            StateController stateController,
            SettingsController settingsController,
            SyncIORequestLinkedList insertOneEntryQueue,
            SyncStringLinkedList commitAllEntryQueue,
            MeasurementController measurementController
    ){
        this.stateController = stateController;
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
                if(settingsController.getString("executorType").equals("single")){
                    insertOneExecutor = new StorageInsertOneExecutor(insertOneEntryQueue,platform,stateController,measurementController);
                    commitAllExecutor = null;
                    new Thread(insertOneExecutor).start();
                }else if(settingsController.getString("executorType").equals("multiple")){
                    commitAllExecutor = new StorageCommitAllExecutor(commitAllEntryQueue,platform,stateController,settingsController,measurementController);
                    insertOneExecutor = null;
                    new Thread(commitAllExecutor).start();
                }else{
                    throw new Exception("executorType missing from settings or has the wrong value");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}