package server.inner_modules.data_transfer_technique;

import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.endpoints.Read;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.data_transfer_technique.techniques.*;
import server.inner_modules.service_time_creators.ServiceTimeCreatorBuilder;
import server.inner_modules.transmitters.ParentTransmitter;


public class TechniqueBuilder {
    public static ParentDataTransferTechnique build(
            SyncIORequestLinkedList ioEntryList,
            ReadyLists readyLists,
            StateController stateController,
            SettingsController settingsController,
            ParentTransmitter transmitter
    ){
        ParentDataTransferTechnique technique;
        String techniqueName = settingsController.getSetting("dataTransferTechnique").toString();
        techniqueName = techniqueName.toUpperCase();
        switch(techniqueName){
            case "A":
                technique = new TechniqueA();
                break;
            case "B":
                technique = new TechniqueB();
                break;
            case "C":
                technique = new TechniqueC();
                break;
            case "D":
                technique = new TechniqueD();
                break;
            case "E":
                technique = new TechniqueE();
                break;
            default:
                technique = new ParentDataTransferTechnique();
        }

        technique.setIoEntryList(ioEntryList);
        technique.setReadyLists(readyLists);
        technique.setStateController(stateController);
        technique.setSettingsController(settingsController);
        technique.setParentServiceTimeCreator(ServiceTimeCreatorBuilder.build(settingsController));
        technique.setTransmitter(transmitter);
        return technique;
    }
}
