package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueD {
    boolean performTransfer;
    int batchId;
    int appId;


    public boolean initialize(){
        performTransfer = false;
        return true;
    }


  
    public void transmit(){
        /*
        SyncIORequestLinkedList requestToTransmit = buffer.removeAllFromApp(batchId,appId);
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
        */
    }

}
