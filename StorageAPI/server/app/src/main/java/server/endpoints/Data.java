package server.endpoints;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.enumerators.PROGRAM_STATE;
import server.enumerators.TIMESTAMP_NAME;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.enumerators.TIMESTAMP_NAME.*;

import java.io.InputStream;

public class Data implements HttpHandler {
    private StateController stateController;
    private SettingsController settingsController;
    private MeasurementController measurementController;
    private SyncIORequestLinkedList IoEntryList;
    private long requestNum;

    public Data(StateController stateController, SettingsController settingsController, MeasurementController measurementController, SyncIORequestLinkedList IoEntryList){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.measurementController = measurementController;
        this.IoEntryList = IoEntryList;
        this.requestNum = 0;
    }


    @Override
    public void handle(HttpExchange t){
        Long arrival_time = System.nanoTime();

        if(settingsController.getIsVerbose()){
            System.out.println("data endpoint reached. IORequest Id: " + requestNum);
        }

        int returnCode;
        if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
        	System.out.println("data endpoint: progran us running");
            InputStream input_stream = t.getRequestBody();
            try{
                byte[] content = input_stream.readAllBytes();

                //to IORequest
                IORequest req = new IORequest(requestNum++,content);
                Long enqueueTime = System.nanoTime();

                //add time measurements to IO request
                req.addTimeStamp(TIMESTAMP_NAME.STORAGE_API_ENTRY,arrival_time);
                req.addTimeStamp(TIMESTAMP_NAME.ENTRY_LIST_ENTRY,enqueueTime);

                //add IO Request to IoEntryList
                IoEntryList.add(req);
                System.out.println("data endpoint: added request to entry list");
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
