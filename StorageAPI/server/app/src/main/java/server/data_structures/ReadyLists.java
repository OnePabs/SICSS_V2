package server.data_structures;

import server.enumerators.TIMESTAMP_NAME;
import server.inner_modules.StateController;

import java.util.Hashtable;
import java.util.Set;

public class ReadyLists {
    private Hashtable<Byte,Hashtable<Byte,SyncIORequestLinkedList>> readylists;
    private StateController stateController;

    public ReadyLists(StateController stateController){
        readylists = new Hashtable<Byte,Hashtable<Byte,SyncIORequestLinkedList>>();
        this.stateController = stateController;
    }

    synchronized public void add(IORequest request, Byte batchId, Byte appId){
        SyncIORequestLinkedList applicationIORequestList;
        if(!readylists.containsKey(batchId) || readylists.get(batchId)==null){
            //batch does not exist. create it
            readylists.put(batchId, new Hashtable<Byte,SyncIORequestLinkedList>());
        }
        Hashtable<Byte,SyncIORequestLinkedList> batch = readylists.get(batchId); //get batch
        if(!batch.containsKey(appId) || batch.get(appId)==null){
            //batch does not have an application with that id. create it
            batch.put(appId, new SyncIORequestLinkedList(appId,stateController));
        }
        applicationIORequestList = batch.get(appId);
        request.addTimeStamp(TIMESTAMP_NAME.READY_LIST_ENTRY);
        applicationIORequestList.add(request);
    }

    synchronized public void add(IORequest request){
        Byte batchId = request.getBatchId();
        Byte appId = request.getAppId();
        add(request,batchId,appId);
    }

    synchronized public SyncIORequestLinkedList removeAllFromApp(Byte batchId, Byte appId){
        if(readylists.containsKey(batchId)){
            Hashtable<Byte,SyncIORequestLinkedList> batch = readylists.get(batchId); //get batch
            return batch.remove(appId);
        }
        return null;
    }

    synchronized public SyncIORequestLinkedList getAndRemoveAllFromBatch(Byte batchId){
        SyncIORequestLinkedList requestsList = new SyncIORequestLinkedList(batchId,stateController);
        if(readylists.containsKey(batchId)){
            Hashtable<Byte,SyncIORequestLinkedList> batch = readylists.get(batchId); //get batch
            Set<Byte> appListIds = batch.keySet();
            for(Byte appListId : appListIds){
                requestsList.add(removeAllFromApp(batchId,appListId));
            }
        }
        return requestsList;
    }

    synchronized public SyncIORequestLinkedList getAndRemoveFromAllBatches(){
        SyncIORequestLinkedList requestsList = new SyncIORequestLinkedList((byte)0,stateController);
        Set<Byte> batchesIds = readylists.keySet();
        for(Byte batchId : batchesIds){
            requestsList.add(getAndRemoveAllFromBatch(batchId));
        }
        return requestsList;
    }

    synchronized public SyncIORequestLinkedList getAndRemoveAllFromBatch(){
        Byte batchId = (byte)0;
        return getAndRemoveAllFromBatch(batchId);
    }

    synchronized public void clear(){
        readylists.clear();
    }

}
