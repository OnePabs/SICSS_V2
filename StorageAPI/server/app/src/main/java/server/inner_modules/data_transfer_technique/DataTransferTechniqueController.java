package server.inner_modules.data_transfer_technique;

import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

public class DataTransferTechniqueController implements Runnable{
    private StateController stateController;
    private SettingsController settingsController;
    private ParentDataTransferTechnique dataTransferTechnique = null;
    private SyncIORequestLinkedList ioEntryList;
    private boolean stop = false;

    public DataTransferTechniqueController(){}

    public void setSettingsController(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void setStateController(StateController stateController){
        this.stateController = stateController;
    }

    public void setIoEntryList(SyncIORequestLinkedList ioEntryList) {
        this.ioEntryList = ioEntryList;
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

                //create new data transfer with new settings
                dataTransferTechnique = TechniqueBuilder.build(ioEntryList,stateController,settingsController);
                Thread dtt = new Thread(dataTransferTechnique);
                dtt.start();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
