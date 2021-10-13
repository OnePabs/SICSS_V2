package server.endpoints;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
    private SyncIORequestLinkedList IoEntryList;

    public Data(StateController stateController, SettingsController settingsController, MeasurementController measurementController, SyncIORequestLinkedList IoEntryList){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.measurementController = measurementController;
        this.IoEntryList = IoEntryList;
    }


    @Override
    public void handle(HttpExchange t){
        Long arrival_time = System.nanoTime();

        if(settingsController.getIsVerbose()){
            System.out.println("data endpoint reached");
        }

        int returnCode;
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
            InputStream input_stream = t.getRequestBody();
            try{
                byte[] content = input_stream.readAllBytes();
                if(settingsController.getIsVerbose()){
                    System.out.print("The byte content received is: ");
                    StringBuilder sb = new StringBuilder();
                    for (byte b : content) {
                        sb.append(String.format("%02X ", b));
                    }
                    System.out.println(sb.toString());
                }

                //to IORequest
                IORequest req = new IORequest(content);
                Long enqueueTime = System.nanoTime();

                //add time measurements to Measurement controller
                measurementController.addMeasurement("arrival_time",arrival_time);
                measurementController.addMeasurement("entry_List_arrival_time",enqueueTime);

                //add IO Request to IoEntryList
                IoEntryList.add(req);
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
