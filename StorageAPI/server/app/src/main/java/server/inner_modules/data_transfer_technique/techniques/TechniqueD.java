package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueD extends ParentDataTransferTechnique {
    boolean performTransfer;
    byte batchId;
    byte appId;

    @Override
    public boolean initialize(){
        performTransfer = false;
        return true;
    }

    @Override
    public boolean isTransferConditionSatisfied(){
        if(performTransfer){
            performTransfer = false; //reset
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void actualize(IORequest request){
        performTransfer = request.isApplicationComplete();
        if(performTransfer){
            System.out.println("app complete");
            batchId = request.getBatchId();
            appId = request.getAppId();
        }
    }


    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = readyLists.removeAllFromApp(batchId,appId);
        if(requestToTransmit == null){
            if(settingsController.getIsVerbose()){
                System.out.println("Technique D: requestToTransmit is null");
            }
            return;
        }
        int size = requestToTransmit.getSize();

        if(transmitter == null){
            System.out.println("Technique D: transmitter null");
            return;
        }
        transmitter.transmit(requestToTransmit);
        if(settingsController.getIsVerbose()){
            System.out.println("Technique D transmitted " + size + " IO Requests.");
        }
    }

}
