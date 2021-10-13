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
    public void transmit(){
        System.out.println("Technique A Transmits an IO Request.");
    }
}
