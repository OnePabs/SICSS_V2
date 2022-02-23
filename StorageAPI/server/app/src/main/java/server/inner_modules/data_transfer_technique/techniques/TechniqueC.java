package server.inner_modules.data_transfer_technique.techniques;

import server.JsonAPI;
import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

import java.util.Hashtable;

public class TechniqueC extends ParentDataTransferTechnique {
    private Long period;
    private Long periodStartTime = null;
    private Long maxSize;
    private Long currentSize;

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
    public boolean isTransferConditionSatisfied(){ //condition for sending ready IO requests
        //System.out.println("is Transfer ready current size: " + currentSize + "       max size: " + maxSize);
        boolean isTransferReady = false;
        if((System.currentTimeMillis() - periodStartTime) > period){
            //max period has elapsed
            isTransferReady = true;
        }else if(currentSize >= maxSize){
            //maximum size reached
            isTransferReady = true;
        }

        if(isTransferReady){
            periodStartTime = System.currentTimeMillis();
            currentSize = (long)0;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void actualize(IORequest request){
        currentSize += request.getContent().length;
        //System.out.println("Actualize: current size: " + currentSize);
    }


    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = readyLists.getAndRemoveFromAllBatches();
        if(requestToTransmit == null){
            if(settingsController.getIsVerbose()){
                System.out.println("Technique C: requestToTransmit is null");
            }
            return;
        }
        int size = requestToTransmit.getSize();

        if(transmitter == null){
            System.out.println("Technique C: transmitter null");
            return;
        }
        transmitter.transmit(requestToTransmit);
        if(settingsController.getIsVerbose()){
            System.out.println("Technique C transmitted " + size + " IO Requests.");
        }
    }
}
