package server;

import com.sun.net.httpserver.HttpServer;
import server.data_structures.SyncIORequestLinkedList;
import server.endpoints.*;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.net.InetSocketAddress;

public class InnerModulesContainer {
    public int portNumber;
    public HttpServer server = null;
    public StateController stateCtrl = null;
    public SettingsController settingsCtrl = null;
    public MeasurementController measurementController = null;
    public SyncIORequestLinkedList IoEntryList = null;

    public InnerModulesContainer(int portNumber){
        this.portNumber = portNumber;
    }

    public void start(){
        server.start();
    }


    public boolean init(){
        return initInnerModules() &&
               initServer()
               ;
    }

    public boolean initInnerModules(){
        stateCtrl = new StateController();
        settingsCtrl = new SettingsController();
        measurementController = new MeasurementController();
        IoEntryList = new SyncIORequestLinkedList((byte)0);

        stateCtrl.setSttingsController(settingsCtrl);
        settingsCtrl.setStateController(stateCtrl);
        measurementController.setStateController(stateCtrl);
        IoEntryList.setStateController(stateCtrl);

        return true;
    }

    public boolean initServer(){
        try{
            server = HttpServer.create(new InetSocketAddress("localhost",portNumber), 0);
            server.createContext("/settings", new Settings(settingsCtrl));
            server.createContext("/start", new Start(stateCtrl,settingsCtrl));
            server.createContext("/stop", new Stop(stateCtrl,settingsCtrl));
            server.createContext("/data", new Data(stateCtrl,settingsCtrl,measurementController,IoEntryList));
            server.setExecutor(null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
