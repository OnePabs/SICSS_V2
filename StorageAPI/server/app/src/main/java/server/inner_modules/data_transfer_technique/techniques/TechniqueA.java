package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

public class TechniqueA extends ParentDataTransferTechnique{
    public TechniqueA(
        StateController stateController, 
        SettingsController settingsController,
        ReadyLists buffer,
        TransmitionInformationObject transmitionInformationObject
    ) {
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueA";
    }
/*
    @Override
    public void transmit(){
        SyncIORequestLinkedList requestToTransmit = buffer.getAndRemoveFromAllBatches();
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
    */
}
