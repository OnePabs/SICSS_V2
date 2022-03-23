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
    private MeasurementController measurementController;

    public StorageCommitAllExecutor(
        SyncStringLinkedList entryQueue, 
        ParentStoragePlatform platform, 
        StateController stateController,
        MeasurementController measurementController){
        this.entryQueue = entryQueue;
        this.platform = platform;
        this.stateController = stateController;
        this.measurementController = measurementController;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                if(stateController.isStateRunning()){
                    String requests = entryQueue.take();
                    if(requests != null){
                        long queueExitTime = System.nanoTime();

                        //parse all requests and add their measurements
                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(requests);
                        JSONArray arr = (JSONArray)obj;
                        long requestId;
                        for (int i=0; i < arr.size(); i++) {
                            JSONObject jobj = (JSONObject)arr.get(i);
                            requestId = (long)jobj.get("requestId");
                            measurementController.addMeasurement(new TimeStamp(((int)requestId),"QueueExitTime",queueExitTime));
                        }

                        // commit all
                        platform.commitAll(requests);

                        //exit time stamp
                        long exitTime = System.nanoTime();

                        //add exit measurements
                        for (int i=0; i < arr.size(); i++) {
                            JSONObject jobj = (JSONObject)arr.get(i);
                            requestId = (long)jobj.get("requestId");
                            measurementController.addMeasurement(new TimeStamp(((int)requestId),"EXIT",exitTime));
                        }
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