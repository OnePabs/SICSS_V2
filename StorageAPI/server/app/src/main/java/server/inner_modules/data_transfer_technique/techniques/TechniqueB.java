package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import server.JsonAPI;

import java.util.Hashtable;

public class TechniqueB extends ParentDataTransferTechnique {
    private Long period;
    private Long periodStartTime = null;

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
        return true;
    }

    @Override
    public boolean isTransferConditionSatisfied(){
        if(periodStartTime == null){
            periodStartTime = Long.valueOf(System.currentTimeMillis());
            return false;
        }else if((System.currentTimeMillis() - periodStartTime) > period){
            return true;
        }else{
            return false;
        }
    } //condition for sending ready IO requests

    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = readyLists.getAndRemoveFromAllBatches();
        if(requestToTransmit == null){
            System.out.println("requestToTransmit is null");
        }

        if(transmitter == null){
            System.out.println("transmitter null");
        }
        transmitter.transmit(requestToTransmit);
        System.out.println("Technique B transmitted IO Requests.");
    }
}
