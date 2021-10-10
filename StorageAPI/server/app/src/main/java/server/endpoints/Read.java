package server.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.enumerators.PROGRAM_STATE;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.io.OutputStream;

public class Read implements HttpHandler {
    private StateController stateController;
    private SettingsController settingsController;
    private MeasurementController measurementController;

    public Read(StateController stateController, SettingsController settingsController, MeasurementController measurementController) {
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.measurementController = measurementController;
    }

    @Override
    public void handle(HttpExchange t){
        if(settingsController.getIsVerbose()){
            System.out.println("Received command to read settings...");
        }

        int returnCode;
        if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED){
            try{
                OutputStream outputStream = t.getResponseBody();

                //build response
                //ToDo
                String jsonStr = "";


                //send response
                t.sendResponseHeaders(200, jsonStr.length());
                outputStream.write(jsonStr.getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                if(settingsController.getIsVerbose()){
                    e.printStackTrace();
                }
                returnCode = 500;
            }
        }else {
            if(settingsController.getIsVerbose()) {
                System.out.println("Cannot read settings,  state does not allow it");
            }
            returnCode = 400;
        }
    }

}
