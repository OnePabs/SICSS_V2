package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueE extends ParentDataTransferTechnique {
    boolean performTransfer;
    int batchId;

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
        performTransfer = request.isBatchComplete();
        if(performTransfer){
            batchId = request.getBatchId();
        }
    }


    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = readyLists.getAndRemoveAllFromBatch(batchId);
        if(requestToTransmit == null){
            if(settingsController.getIsVerbose()){
                System.out.println("Technique E: requestToTransmit is null");
            }
            return;
        }
        int size = requestToTransmit.getSize();

        if(transmitter == null){
            System.out.println("Technique E: transmitter null");
            return;
        }
        transmitter.transmit(requestToTransmit);
        if(settingsController.getIsVerbose()){
            System.out.println("Technique E transmitted " + size + " IO Requests.");
        }
    }

}
