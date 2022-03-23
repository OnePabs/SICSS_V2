package StorageManager.Endpoints;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import StorageManager.enumerators.PROGRAM_STATE;
import StorageManager.SettingsController;
import StorageManager.StateController;

public class Stop implements HttpHandler{
    private StateController stateController;
    private SettingsController settingsController;

    public Stop(StateController stateController,SettingsController settingsController){
        this.stateController = stateController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t){

        int returnCode;
        if(stateController.changeState(PROGRAM_STATE.STOPPED)){
            returnCode = 200;
        }else{
            returnCode = 400;
        }

        try{
            t.sendResponseHeaders(returnCode,-1);
        }catch (IOException ioe){
            if(settingsController.getIsVerbose()){
                System.out.println("Error sending code " + returnCode);
                ioe.printStackTrace();
            }
        }
    }
}
