package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import StorageManager.*;

public class InsertOne implements HttpHandler{
    private boolean  isVerbose;
    private MysqlApi mysqlapi;
    private MeasurementController measurementController;

    public InsertOne(MysqlApi mysqlapi, MeasurementController measurementController, boolean  isVerbose){
        this.mysqlapi = mysqlapi;
        this.isVerbose = isVerbose;
        this.measurementController = measurementController;
    }

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        measurementController.addMeasurement(new TimeStamp(0,"ENTRY",System.nanoTime()));
        if(isVerbose){
            System.out.println("Storage Manager: InsertOne endpoint reached");
        }

        boolean success = false;
        Statement stmt = null;
        InputStream input_stream = t.getRequestBody();


        try{
            byte[] content = input_stream.readAllBytes();
            success = mysqlapi.insertone(content);
        }catch(Exception e){
            success = false;
            e.printStackTrace();
        }


        try{
            if(success){
                t.sendResponseHeaders(200,-1);
                measurementController.addMeasurement(new TimeStamp(0,"EXIT",System.nanoTime()));
            }else{
                t.sendResponseHeaders(400,-1);
            }

        }catch (Exception ioe){
            System.out.println("Error: ");
            ioe.printStackTrace();
        }
    }
}