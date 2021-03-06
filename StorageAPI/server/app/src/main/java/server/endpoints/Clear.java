package server.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.io.IOException;

public class Clear implements HttpHandler {
    private SettingsController settingsController;
    private StateController stateController;
    private MeasurementController measurementController;
    private SyncIORequestLinkedList ioEntryList;
    private ReadyLists buffer;

    public Clear(StateController stateController,
                 SettingsController settingsController,
                 MeasurementController measurementController,
                 SyncIORequestLinkedList ioEntryList,
                 ReadyLists buffer) {
        this.settingsController = settingsController;
        this.stateController = stateController;
        this.measurementController = measurementController;
        this.ioEntryList = ioEntryList;
        this.buffer = buffer;
    }

    @Override
    public void handle(HttpExchange exchange) {
        if(settingsController.getIsVerbose()){
            System.out.println("Clear endpoint reached");
        }
        int returnCode;
        if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED){
            measurementController.clear();
            ioEntryList.clearList();
            buffer.clear();
            returnCode = 200;
        }else{
            returnCode = 400;
        }

        try{
            exchange.sendResponseHeaders(returnCode,-1);
        }catch (Exception e){
            if(settingsController.getIsVerbose()){
                e.printStackTrace();
            }
        }

    }
}
