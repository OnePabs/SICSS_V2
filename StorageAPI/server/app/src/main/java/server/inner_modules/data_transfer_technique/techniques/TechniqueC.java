package server.inner_modules.data_transfer_technique.techniques;

import server.JsonAPI;
import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

import java.util.Hashtable;


public class TechniqueC extends ParentDataTransferTechnique {
    private Long period;
    private Long periodStartTime = null;
    private Long maxSize;
    private Long currentSize;

    public TechniqueC(
        StateController stateController, 
        SettingsController settingsController,
        ReadyLists buffer,
        TransmitionInformationObject transmitionInformationObject
    ){
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueC";
    }

    @Override
    public boolean initialize(){
        if(!settingsController.containsSetting("dataTransferTechniqueSettings")){
            return false;
        }
        Hashtable<String,Object> dtSettings = JsonAPI.jsonToHashTable(settingsController.getSetting("dataTransferTechniqueSettings"));
        if(!dtSettings.containsKey("period") || !dtSettings.containsKey("maxsize") ){
            return false;
        }
        period = (Long)dtSettings.get("period");
        periodStartTime = Long.valueOf(System.currentTimeMillis());
        maxSize = (Long)dtSettings.get("maxsize");
        currentSize = (long)0;
        if(settingsController.getIsVerbose()){
            System.out.println("Technique C maxsize:" + maxSize + " period: " + period);
        }
        return true;
    }



    @Override
    public void waitForDataTransferCondition() throws Exception{ //condition for sending ready IO requests
        int numBytes = buffer.getNumberOfBytesInBuffer();
        long start_time = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start_time;
        while(numBytes < maxSize && duration<period){
            super.waitForDataTransferCondition();
            numBytes = buffer.getNumberOfBytesInBuffer();
        }
    }
}
