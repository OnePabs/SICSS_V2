package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import java.nio.charset.StandardCharsets;

public class TechniqueA extends ParentDataTransferTechnique{
    boolean sendIORequest = false;
    IORequest request;

    public TechniqueA() {
        super();
    }

    @Override
    public boolean isTransferConditionSatisfied(){
        boolean last = sendIORequest;
        sendIORequest = !last;
        return last;
    }

    @Override
    public void handle(){
        System.out.println("Technique A handling an IO Request.");
        try{
            request = this.ioEntryList.take();
            String reqBody = new String(request.content, StandardCharsets.UTF_8);
            System.out.println("Technique A - Request body: " + reqBody);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void transmit(){
        System.out.println("Technique A Transmits an IO Request.");
    }

    @Override
    public void clear(){
        System.out.println("Technique A clear: nothing to clear");
    }

}
