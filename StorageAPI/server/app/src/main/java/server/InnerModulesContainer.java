package server;

import com.sun.net.httpserver.HttpServer;
import server.data_structures.SyncIORequestLinkedList;
import server.endpoints.*;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.data_transfer_technique.DataTransferTechniqueController;

import java.net.InetSocketAddress;

public class InnerModulesContainer {
    public int portNumber;
    public HttpServer server = null;
    public StateController stateCtrl = null;
    public SettingsController settingsCtrl = null;
    public MeasurementController measurementController = null;
    public SyncIORequestLinkedList ioEntryList = null;
    public DataTransferTechniqueController dataTransferTechniqueController;

    public InnerModulesContainer(int portNumber){
        this.portNumber = portNumber;
    }

    public void start(){
        Thread dataTransferControllerThread = new Thread(dataTransferTechniqueController);
        dataTransferControllerThread.start();
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
        ioEntryList = new SyncIORequestLinkedList((byte)0);
        dataTransferTechniqueController = new DataTransferTechniqueController();

        stateCtrl.setSttingsController(settingsCtrl);
        settingsCtrl.setStateController(stateCtrl);
        measurementController.setStateController(stateCtrl);
        ioEntryList.setStateController(stateCtrl);
        dataTransferTechniqueController.setStateController(stateCtrl);
        dataTransferTechniqueController.setSettingsController(settingsCtrl);
        dataTransferTechniqueController.setIoEntryList(ioEntryList);

        return true;
    }

    public boolean initServer(){
        try{
            server = HttpServer.create(new InetSocketAddress("localhost",portNumber), 0);
            server.createContext("/settings", new Settings(settingsCtrl));
            server.createContext("/start", new Start(stateCtrl,settingsCtrl));
            server.createContext("/stop", new Stop(stateCtrl,settingsCtrl));
            server.createContext("/data", new Data(stateCtrl,settingsCtrl,measurementController, ioEntryList));
            server.setExecutor(null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
