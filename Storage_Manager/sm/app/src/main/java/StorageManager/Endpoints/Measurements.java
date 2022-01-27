package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import StorageManager.*;

import java.io.IOException;
import java.io.OutputStream;

public class Measurements implements HttpHandler {
    private SettingsController settingsController;
    private MeasurementController measurementController;
    private boolean isVerbose;

    public Measurements(MeasurementController measurementController, SettingsController settingsController){
        this.measurementController = measurementController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        if(settingsController.getIsVerbose()){
            System.out.println("Measurements endpoint reached");
        }

        TimeStamp[] timeStamps = measurementController.getMeasurements();

        JSONArray cont = new JSONArray();
        if(settingsController.getIsVerbose()){
            System.out.println("Number of measurements: " + timeStamps.length);
        }
        for(int i=0;i<timeStamps.length;i++){
            TimeStamp tp = timeStamps[i];
            JSONObject inner = new JSONObject();
            inner.put("requestId",tp.getRequestId());
            inner.put("TIMESTAMP_NAME",tp.getName());
            inner.put("timeStamp",tp.getTimeStamp());
            cont.add(inner);
        }
        String jsonArrStr = cont.toJSONString();

        try{
            t.sendResponseHeaders(200, jsonArrStr.length());
            OutputStream os = t.getResponseBody();
            os.write(jsonArrStr.getBytes());
            os.close();
        }catch(Exception e){
            e.printStackTrace();
            t.sendResponseHeaders(400, -1);
        }

    }
}
