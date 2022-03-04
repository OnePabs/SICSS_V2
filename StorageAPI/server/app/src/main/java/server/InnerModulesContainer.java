package server;

import com.sun.net.httpserver.HttpServer;
import server.data_structures.*;
import server.endpoints.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.*;
import server.inner_modules.transmitters.*;

import java.net.InetSocketAddress;

public class InnerModulesContainer implements Runnable {
    private int portNumber;
    private HttpServer server = null;
    private StateController stateCtrl = null;
    private SettingsController settingsCtrl = null;
    private MeasurementController measurementController = null;
    private SyncIORequestLinkedList ioEntryList = null;
    private Sorter sorter = null;
    private ReadyLists buffer = null;
    private ParentDataTransferTechnique dataTransferTechnique = null;
    private TransmitionInformationObject transmitionInformationObject = null;
    private ParentTransmitter transmitter = null;
    boolean isRunning = true;

    public InnerModulesContainer(int portNumber){
        this.portNumber = portNumber;
    }

    private void startImmutableThreads(){
        //starts threads that are never replaced

        //start sorter
        Thread sorterThread = new Thread(sorter);
        sorterThread.start();

        //start server
        server.start();
    }


    private boolean init(){
        return initInnerModules() &&
               initServer()
               ;
    }

    private boolean initInnerModules(){
        stateCtrl = new StateController();
        settingsCtrl = new SettingsController();
        stateCtrl.setSttingsController(settingsCtrl);
        settingsCtrl.setStateController(stateCtrl);
        
        measurementController = new MeasurementController(stateCtrl);
        ioEntryList = new SyncIORequestLinkedList((byte)0,stateCtrl);
        buffer = new ReadyLists(stateCtrl);
        sorter = new Sorter(ioEntryList, buffer, settingsCtrl, stateCtrl);
        transmitionInformationObject = new TransmitionInformationObject();

        return true;
    }

    private boolean initServer(){
        try{
            server = HttpServer.create(new InetSocketAddress("0.0.0.0",portNumber), 0);
            server.createContext("/settings", new Settings(stateCtrl,settingsCtrl));
            server.createContext("/start", new Start(stateCtrl,settingsCtrl));
            server.createContext("/stop", new Stop(stateCtrl,settingsCtrl));
            server.createContext("/data", new Data(stateCtrl,settingsCtrl,measurementController, ioEntryList));
            server.createContext("/measurements", new Measurements(stateCtrl,settingsCtrl,measurementController));
            server.createContext("/clear", new Clear(stateCtrl,settingsCtrl,measurementController,ioEntryList,buffer));
            server.setExecutor(null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void run()  {
        this.init();
        this.startImmutableThreads();

        while(isRunning){
            synchronized (settingsCtrl.changaOfSettingsNotifier){
                try{
                    settingsCtrl.changaOfSettingsNotifier.wait();
                }catch(Exception e){
                    System.out.println("Exception when waiting for settings change");
                    e.printStackTrace();
                    return;
                }
            }

            //2 threads to update: Data transfer technique, Transmitter

            //stop the currently running data transfer technique
            if(dataTransferTechnique != null){
                dataTransferTechnique.finishExecution();
            }

            //stop the currently running transmitter
            if(transmitter != null){
                transmitter.finishExecution();
            }

            //empty readyLists
            buffer.clear();

            //create new transmitter
            transmitter = TransmitterBuilder.build(buffer,stateCtrl,settingsCtrl,transmitionInformationObject,measurementController);
            Thread transmitterThread = new Thread(transmitter);
            transmitterThread.start();

            //create new data transfer with new settings
            dataTransferTechnique = TechniqueBuilder.build(stateCtrl,settingsCtrl,buffer,transmitionInformationObject);
            Thread dtt = new Thread(dataTransferTechnique);
            dtt.start();
        }
    }


    public void stop(){
        isRunning = false;
    }
}
