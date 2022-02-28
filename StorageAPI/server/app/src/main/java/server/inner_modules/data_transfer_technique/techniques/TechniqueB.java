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
        if(settingsController.getIsVerbose()){
            System.out.println("Technique B period: " + period + ". value of period setting: " + dtSettings.get("period").toString());
        }
        return true;
    }

    @Override
    public boolean isTransferConditionSatisfied(){
        if(periodStartTime == null){
            periodStartTime = Long.valueOf(System.currentTimeMillis());
            return false;
        }else if((System.currentTimeMillis() - periodStartTime) > period){
            periodStartTime = System.currentTimeMillis();
            return true;
        }else{
            return false;
        }
    } //condition for sending ready IO requests

    @Override
    public void transmit() throws Exception{
        System.out.println("Technique B: Period elapsed");
        SyncIORequestLinkedList requestToTransmit = readyLists.getAndRemoveFromAllBatches();
        if(requestToTransmit == null){
            //Ready List ERROR
            throw new Exception("Technique B: Ready List getAndRemoveFromAllBatches returned null");
        }else if(transmitter == null){
            //Transmitter ERROR
            throw new Exception("Technique B: Transmitter is null");
        }else if(requestToTransmit.getSize() == 0){
            //No requests in Buffer
            System.out.println("Technique B: No requests in buffer. Data transfer canaceled");
            if(settingsController.getIsVerbose()){
                System.out.println("Technique B: No requests in buffer. Data transfer canaceled");
            }
        }else{
            //transmit requests in buffer
            int size = requestToTransmit.getSize();
            if(settingsController.getIsVerbose()){
                System.out.println("Technique B sending " + size + " IO Requests to transmitter");
            }
            transmitter.transmit(requestToTransmit);
        }
    }
}
