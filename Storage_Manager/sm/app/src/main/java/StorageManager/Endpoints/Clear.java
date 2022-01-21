package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import StorageManager.*;


import java.io.IOException;
import java.io.OutputStream;

public class Clear implements HttpHandler {
    private MeasurementController measurementController;
    private boolean isVerbose;
    private MysqlApi mysqlapi;

    public Clear(MeasurementController measurementController, MysqlApi mysqlapi, boolean isVerbose){
        this.measurementController = measurementController;
        this.isVerbose = isVerbose;
        this.mysqlapi = mysqlapi;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        if(isVerbose){
            System.out.println("clear endpoint reached");
        }

        measurementController.clear();
        mysqlapi.clear();

        try{
            t.sendResponseHeaders(200,-1);
        }catch(Exception e){
            e.printStackTrace();
            t.sendResponseHeaders(400, -1);
        }

    }
}
