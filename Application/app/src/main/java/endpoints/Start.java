package endpoints;

import Application.SettingsController;
import Application.StateController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enumerators.PROGRAM_STATE;

import java.io.IOException;

public class Start implements HttpHandler {
    private StateController stateController;
    private SettingsController settingsController;

    public Start(StateController stateController, SettingsController settingsController){
        this.stateController = stateController;
        this.settingsController = settingsController;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        if(this.settingsController.getIsVerbose()){
            System.out.println("Start endpoint reached");
        }

        int returnCode;
        if(stateController.changeState(PROGRAM_STATE.RUNNING)){
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
