package server.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import server.data_structures.TimeStamp;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.io.IOException;
import java.io.OutputStream;

public class Measurements implements HttpHandler {
    private SettingsController settingsController;
    private StateController stateController;
    private MeasurementController measurementController;

    public Measurements(StateController stateController, SettingsController settingsController,MeasurementController measurementController){
        this.settingsController = settingsController;
        this.stateController = stateController;
        this.measurementController = measurementController;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        if(settingsController.getIsVerbose()){
            System.out.println("Measurements endpoint reached");
        }

        if(stateController.getCurrentState()== PROGRAM_STATE.STOPPED){
            TimeStamp[] timeStamps = measurementController.getMeasurements();

            JSONArray cont = new JSONArray();
            System.out.println("Number of measurements: " + timeStamps.length);
            for(int i=0;i<timeStamps.length;i++){
                TimeStamp tp = timeStamps[i];
                JSONObject inner = new JSONObject();
                inner.put("requestId",tp.getRequestId());
                inner.put("TIMESTAMP_NAME",tp.getName().toString());
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
                if(settingsController.getIsVerbose()){
                    e.printStackTrace();
                }
                t.sendResponseHeaders(400, -1);
            }
        }
    }
}
