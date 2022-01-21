package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueA extends ParentDataTransferTechnique{
    boolean sendIORequest = false;
    IORequest request;

    public TechniqueA() {
        super();
        techniqueName = "techniqueA";
    }

    @Override
    public boolean isTransferConditionSatisfied(){
        boolean last = sendIORequest;
        sendIORequest = !last;
        return last;
    }

    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = readyLists.getAndRemoveFromAllBatches();
        if(requestToTransmit == null){
            System.out.println("requestToTransmit is null");
        }else if(requestToTransmit.getSize() != 1){
            System.out.println("Technique A: number of requests to transmit is not one");
        }else if(transmitter == null){
            System.out.println("transmitter null");
        }else{
            try{
                IORequest request = requestToTransmit.take();
                transmitter.transmit(request);
                //System.out.println("Technique A transmitted an IO Request.");
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
