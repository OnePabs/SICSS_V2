package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import StorageManager.*;
import StorageManager.data_structures.*;

public class InsertOne implements HttpHandler{
    private MeasurementController measurementController;
    private SettingsController settingsController;
    private SyncIORequestLinkedList insertOneEntryQueue;

    public InsertOne(
            SyncIORequestLinkedList insertOneEntryQueue,
            MeasurementController measurementController,
            SettingsController settingsController){
        this.insertOneEntryQueue = insertOneEntryQueue;
        this.measurementController = measurementController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        long entry_time = System.nanoTime();
        if(settingsController.getIsVerbose()){
            System.out.println("Storage Manager: InsertOne endpoint reached");
        }

        boolean success = false;
        InputStream input_stream = t.getRequestBody();

        try{
            byte[] content = input_stream.readAllBytes();
            IORequest req = new IORequest(content);
            measurementController.addMeasurement(new TimeStamp(req.getRequestId(),"ENTRY",entry_time));
            insertOneEntryQueue.add(req);
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