package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import StorageManager.*;

public class CommitAll implements HttpHandler{
    private boolean  isVerbose;
    private MysqlApi mysqlapi;
    private MeasurementController measurementController;

    public CommitAll(MysqlApi mysqlapi, MeasurementController measurementController, boolean  isVerbose){
        this.mysqlapi = mysqlapi;
        this.isVerbose = isVerbose;
        this.measurementController = measurementController;
    }

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        measurementController.addMeasurement(new TimeStamp(0,"ENTRY",System.nanoTime()));
        if(isVerbose){
            System.out.println("Storage Manager: commitall endpoint reached");
        }

        boolean success = false;
        InputStream input_stream = t.getRequestBody();
        try{
            byte[] content = input_stream.readAllBytes();
            JSONParser parser = new JSONParser();
            String jstr = new String(content,StandardCharsets.UTF_8);
            Object obj = parser.parse(jstr);
            JSONArray arr = (JSONArray)obj;
            success = mysqlapi.comitAll(arr);
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