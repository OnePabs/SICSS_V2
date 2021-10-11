package server.inner_modules.data_transfer_technique;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;

public class ParentDataTransferTechnique implements Runnable{
    private String techniqueName;
    private SyncIORequestLinkedList ioEntryList;

    public ParentDataTransferTechnique(SyncIORequestLinkedList ioEntryList){
        this.ioEntryList = ioEntryList;
    }

    @Override
    public void run() {

    }


    public void Handle(IORequest request){}
}
