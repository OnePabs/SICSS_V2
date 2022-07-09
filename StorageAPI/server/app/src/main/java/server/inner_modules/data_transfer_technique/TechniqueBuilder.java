package server.inner_modules.data_transfer_technique;

import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.endpoints.Read;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.techniques.*;
import server.inner_modules.service_time_creators.ServiceTimeCreatorBuilder;


public class TechniqueBuilder {
    public static ParentDataTransferTechnique build(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject,
            MeasurementController measurementController
    ){
        if(settingsController.getIsVerbose()){
            System.out.println("Techniqe builder");
        }
        ParentDataTransferTechnique technique;
        String techniqueName = settingsController.getSetting("dataTransferTechnique").toString();
        techniqueName = techniqueName.toUpperCase();
        switch(techniqueName){
            case "A":
                technique = new TechniqueA(stateController, settingsController, buffer, transmitionInformationObject);
                break;
            case "B":
                technique = new TechniqueB(stateController, settingsController, buffer, transmitionInformationObject);
                break;
            case "C":
                technique = new TechniqueC(stateController, settingsController, buffer, transmitionInformationObject);
                break;
            case "CA":
                technique = new TechniqueCAdaptive(stateController, settingsController, buffer, transmitionInformationObject,measurementController);
                break;
            default:
                technique = new ParentDataTransferTechnique(stateController, settingsController, buffer, transmitionInformationObject);
        }
        return technique;
    }
}
