package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.io.OutputStream;
import StorageManager.data_structures.*;
import StorageManager.*;

public class Clear implements HttpHandler {
    private SyncIORequestLinkedList insertOneEntryQueue;
    private SyncStringLinkedList commitAllEntryQueue;
    private MeasurementController measurementController;
    private SettingsController settingsController;

    public Clear(
            SyncIORequestLinkedList insertOneEntryQueue,
            SyncStringLinkedList commitAllEntryQueue,
            MeasurementController measurementController,
            SettingsController settingsController){
        this.insertOneEntryQueue = insertOneEntryQueue;
        this.commitAllEntryQueue = commitAllEntryQueue;
        this.measurementController = measurementController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        if(settingsController.getIsVerbose()){
            System.out.println("clear endpoint reached");
        }

        measurementController.clear();
        insertOneEntryQueue.clear();
        commitAllEntryQueue.clear();

        try{
            t.sendResponseHeaders(200,-1);
        }catch(Exception e){
            e.printStackTrace();
            t.sendResponseHeaders(400, -1);
        }

    }
}
