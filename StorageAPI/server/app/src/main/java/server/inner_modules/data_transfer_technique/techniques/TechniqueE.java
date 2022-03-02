package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueE {
    boolean performTransfer;
    int batchId;


    public boolean initialize(){
        performTransfer = false;
        return true;
    }


    public void transmit(){
        /*
        SyncIORequestLinkedList requestToTransmit = buffer.getAndRemoveAllFromBatch(batchId);
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
        */
    }

}
