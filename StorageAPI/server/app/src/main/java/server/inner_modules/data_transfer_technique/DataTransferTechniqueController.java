package server.inner_modules.data_transfer_technique;

import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.transmitters.ParentTransmitter;
import server.inner_modules.transmitters.TransmitterBuilder;

public class DataTransferTechniqueController implements Runnable{
    private StateController stateController;
    private SettingsController settingsController;
    private ParentDataTransferTechnique dataTransferTechnique = null;
    private SyncIORequestLinkedList ioEntryList;
    private ReadyLists readyLists;
    protected ParentTransmitter transmitter;
    protected MeasurementController measurementController;
    private boolean stop = false;

    public DataTransferTechniqueController(
            StateController stateController,
            SettingsController settingsController,
            SyncIORequestLinkedList ioEntryList,
            MeasurementController measurementController
    ){
        this.stateController = stateController;
        this.settingsController = settingsController;
        this.ioEntryList = ioEntryList;
        this.measurementController = measurementController;
    }


    @Override
    public void run() {
        while(!stop){

            try {
                synchronized (settingsController.changaOfSettingsNotifier){
                    settingsController.changaOfSettingsNotifier.wait();
                }

                if(settingsController.getIsVerbose()){
                    System.out.println("Data Transfer Controller: Settings Controller Changed Settings");
                }

                //stop running data transfer technique
                if(dataTransferTechnique != null){
                    dataTransferTechnique.finishExecution();
                }

                //create new readyLists
                readyLists = new ReadyLists(stateController);

                //create new transmitter
                transmitter = TransmitterBuilder.build(readyLists,stateController,settingsController,measurementController);

                //create new data transfer with new settings
                dataTransferTechnique = TechniqueBuilder.build(ioEntryList,readyLists,stateController,settingsController,transmitter);
                Thread dtt = new Thread(dataTransferTechnique);
                dtt.start();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
