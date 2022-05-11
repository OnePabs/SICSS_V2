package StorageManager.executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.nio.charset.StandardCharsets;
import org.json.simple.parser.JSONParser;
import StorageManager.data_structures.*;
import StorageManager.storage_platforms.*;
import StorageManager.*;

public class StorageCommitAllExecutor implements Runnable{
    private boolean isRunning;
    private SyncStringLinkedList entryQueue;
    private ParentStoragePlatform platform;
    private StateController stateController;
    SettingsController settingsController;
    private MeasurementController measurementController;

    public StorageCommitAllExecutor(
        SyncStringLinkedList entryQueue, 
        ParentStoragePlatform platform, 
        StateController stateController,
        SettingsController settingsController,
        MeasurementController measurementController){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.measurementController = measurementController;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                if(stateController.isStateRunning()){
                    String requests = entryQueue.take();
                    if(settingsController.getIsVerbose()){
                        System.out.println("CommitAll Executor: Executor took batch from comit all Entry Queue");
                    }

                    if(requests != null){
                        long queueExitTime = System.nanoTime();

                        //parse all requests and add their measurements
                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(requests);
                        JSONArray arr = (JSONArray)obj;
                        long requestId;
                        if(settingsController.getIsVerbose()){
                            System.out.println("CommitAll Executor: Number of requests in batch = " + arr.size());
                             System.out.println("CommitAll Executor: adding QueueExitTime measurements to measurements controller");
                        }
                        for (int i=0; i < arr.size(); i++) {
                            JSONObject jobj = (JSONObject)arr.get(i);
                            requestId = (long)jobj.get("requestId");
                            measurementController.addMeasurement(new TimeStamp(((int)requestId),"QueueExitTime",queueExitTime));
                        }

                        // commit all
                        platform.commitAll(arr);

                        //exit time stamp
                        long exitTime = System.nanoTime();
                        if(settingsController.getIsVerbose()){
                            System.out.println("CommitAll Executor: Number of requests in batch after service time = " + arr.size());
                            System.out.println("CommitAll Executor: adding EXIT measurements to measurements controller ");
                        }
                        //add exit measurements
                        for (int i=0; i < arr.size(); i++) {
                            JSONObject jobj = (JSONObject)arr.get(i);
                            requestId = (long)jobj.get("requestId");
                            measurementController.addMeasurement(new TimeStamp(((int)requestId),"EXIT",exitTime));
                        }
                    }else{
                        if(settingsController.getIsVerbose()){
                            System.out.println("CommitAll Executor: Batch is null");
                        }
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