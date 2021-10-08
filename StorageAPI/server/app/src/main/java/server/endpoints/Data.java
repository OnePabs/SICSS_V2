package server.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.data_structures.IORequest;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Data implements HttpHandler {
    private StateController stateController;
    private SettingsController settingsController;
    private MeasurementController measurementController;

    public Data(StateController stateController, SettingsController settingsController, MeasurementController measurementController){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.measurementController = measurementController;
    }


    @Override
    public void handle(HttpExchange t){
        Long arrival_time = System.nanoTime();

        int returnCode;
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
            InputStream input_stream = t.getRequestBody();
            try{
                byte[] content = input_stream.readAllBytes();
                if(settingsController.getIsVerbose()){
                    String str = new String(content, StandardCharsets.UTF_8);
                    System.out.println("Data endpoint received: " + str);
                }

                //to IORequest
                IORequest req = new IORequest(content);
                Long enqueueTime = System.nanoTime();

                //add time measurements to Measurement controller
                measurementController.addMeasurement("arrival_time",arrival_time);
                measurementController.addMeasurement("entry_List_arrival_time",arrival_time);

                //Todo: send data to IORequest queue

                returnCode = 200;
            }catch(Exception e){
                if(settingsController.getIsVerbose()){
                    e.printStackTrace();
                }
                returnCode = 500;
            }
        }else{
            returnCode = 400;
        }
        try{
            t.sendResponseHeaders(returnCode,-1);
        }catch (Exception e){
            if(settingsController.getIsVerbose()){
                e.printStackTrace();
            }
        }

    }

}
