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
    private StateController stateController;
    private MeasurementController measurementController;
    private boolean isVerbose;

    public Measurements(MeasurementController measurementController, StateController stateController, SettingsController settingsController){
        this.measurementController = measurementController;
        this.stateController = stateController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t){

        if(settingsController.getIsVerbose()){
            System.out.println("Measurements endpoint reached");
        }

        int responseCode = 400;
        int responseLength = -1;
        String response = null;

        if(stateController.isStateRunning()){
            if(settingsController.getIsVerbose()){
                System.out.println("Measurements Endpoint: Cannot get measurements because state is running");
            }
            responseCode = 400;
        }else{
            TimeStamp[] timeStamps = measurementController.getMeasurements();
            if(timeStamps == null){
                if(settingsController.getIsVerbose()){
                    System.out.println("Measurements Endpoint: measurementController returned NULL");
                }
                responseCode = 400;
            }else if(timeStamps.length == 0){
                if(settingsController.getIsVerbose()){
                    System.out.println("Measurements Endpoint: no measurements recorded");
                }
                responseCode = 400;
            }else{
                JSONArray cont = new JSONArray();
                if(settingsController.getIsVerbose()){
                    System.out.println("Measurements Endpoint: Number of measurements: " + timeStamps.length);
                }
                for(int i=0;i<timeStamps.length;i++){
                    TimeStamp tp = timeStamps[i];
                    JSONObject inner = new JSONObject();
                    inner.put("requestId",tp.getRequestId());
                    inner.put("TIMESTAMP_NAME",tp.getName());
                    inner.put("timeStamp",tp.getTimeStamp());
                    cont.add(inner);
                }
                response = cont.toJSONString();
                responseLength = response.length();
                responseCode = 200;
            }
        }

        try{
            t.sendResponseHeaders(responseCode, responseLength);
            if(responseCode==200){
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
