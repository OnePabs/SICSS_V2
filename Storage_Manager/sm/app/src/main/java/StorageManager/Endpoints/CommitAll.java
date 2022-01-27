package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import StorageManager.*;
import StorageManager.data_structures.*;

public class CommitAll implements HttpHandler{
    private MeasurementController measurementController;
    private SyncStringLinkedList commitAllEntryQueue;
    private SettingsController settingsController;

    public CommitAll(SyncStringLinkedList commitAllEntryQueue, MeasurementController measurementController, SettingsController settingsController){
        this.commitAllEntryQueue = commitAllEntryQueue;
        this.measurementController = measurementController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t) {
        //commit all
        long entry_time = System.nanoTime();
        if(settingsController.getIsVerbose()){
            System.out.println("Storage Manager: commitall endpoint reached");
        }
        boolean success = false;

        InputStream input_stream = t.getRequestBody();
        try{
            byte[] content = input_stream.readAllBytes();
            String jstr = new String(content,StandardCharsets.UTF_8);
            measurementController.addMeasurement(new TimeStamp(0,"ENTRY",entry_time));
            commitAllEntryQueue.add(jstr);
            success = true;
        }catch(Exception e){
            success = false;
            e.printStackTrace();
        }


        try{
            if(success){
                t.sendResponseHeaders(200,-1);
            }else{
                t.sendResponseHeaders(400,-1);
            }

        }catch (Exception ioe){
            System.out.println("Error: ");
            ioe.printStackTrace();
        }
    }
}