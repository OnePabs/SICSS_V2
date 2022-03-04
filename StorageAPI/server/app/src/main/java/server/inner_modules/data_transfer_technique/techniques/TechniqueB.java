package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import server.JsonAPI;

import java.util.Hashtable;

public class TechniqueB extends ParentDataTransferTechnique {
    private Long period;

    public TechniqueB(
            StateController stateController, 
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject
        ){
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueB";
    }

    @Override
    public boolean initialize(){
        if(!settingsController.containsSetting("dataTransferTechniqueSettings")){
            return false;
        }
        Hashtable<String,Object> dtSettings = JsonAPI.jsonToHashTable(settingsController.getSetting("dataTransferTechniqueSettings"));
        if(!dtSettings.containsKey("period")){
            return false;
        }

        period = (Long)dtSettings.get("period");
        if(settingsController.getIsVerbose()){
            System.out.println("Technique B period: " + period + ". value of period setting: " + dtSettings.get("period").toString());
        }
        return true;
    }

    @Override
    public void waitForDataTransferCondition() throws Exception{
        long start = System.currentTimeMillis();
        Thread.sleep(period);
        long periodMeasuredTime = System.currentTimeMillis() - start;

        if(settingsController.getIsVerbose()){
            System.out.println("Technique B period was: "+periodMeasuredTime);
        }
    } //condition for sending ready IO requests
}
