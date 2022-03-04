package server.data_structures;

import server.enumerators.TIMESTAMP_NAME;
import server.inner_modules.StateController;

import java.util.Hashtable;
import java.util.Set;

public class ReadyLists {
    public Object bufferEntryEvent;
    private Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> readylists; //hashtable of batches. Each batch is itself a hashtable of applications
    private StateController stateController;

    public ReadyLists(StateController stateController){
        readylists = new Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>>();
        this.stateController = stateController;
        this.bufferEntryEvent = new Object();
    }

    synchronized public void add(IORequest request){
        Integer batchId = request.getBatchId();
        Integer appId = request.getAppId();

        SyncIORequestLinkedList applicationIORequestList;
        if(!readylists.containsKey(batchId) || readylists.get(batchId)==null){
            //batch does not exist. create it
            readylists.put(batchId, new Hashtable<Integer,SyncIORequestLinkedList>());
        }
        Hashtable<Integer,SyncIORequestLinkedList> batch = readylists.get(batchId); //get batch
        if(!batch.containsKey(appId) || batch.get(appId)==null){
            //batch does not have an application with that id. create it
            batch.put(appId, new SyncIORequestLinkedList(appId,stateController));
        }
        applicationIORequestList = batch.get(appId);
        //request.addTimeStamp(TIMESTAMP_NAME.READY_LIST_ENTRY);
        applicationIORequestList.add(request);
        synchronized(bufferEntryEvent){
            bufferEntryEvent.notifyAll();
        }
    }

    /**
    * Get all requests from an application and clear them from the buffer
    */
    synchronized public SyncIORequestLinkedList removeAllFromApp(Integer batchId, Integer appId){
        Hashtable<Integer,SyncIORequestLinkedList> batch = readylists.get(batchId);//get batch
        if(batch!=null){
            return batch.remove(appId); //removes the mapping in batch hashTable between appId and the SyncIORequestLinkedList. Assumed that no data structure is created
        }
        return null;
    }

    /**
    * Get all Requests from a batch and clear them from the buffer
    */
    synchronized public Hashtable<Integer,SyncIORequestLinkedList> removeAllFromBatch(Integer batchId){
        Hashtable<Integer,SyncIORequestLinkedList> batch = readylists.remove(batchId);
        return batch;
    }


    /**
    * Get all requests in buffer
     */
    synchronized public Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> removeAllFromBuffer(){
        Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> requests = readylists;
        readylists = new Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>>();
        return readylists;
    }

    public static int getNumberOfRequestsInBuffer(Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> buffer){
         if(buffer == null){
            return 0;
        }

        int numRequests = 0;
        Set<Integer> batchesIds = buffer.keySet();
        for(Integer batchId : batchesIds){
            Hashtable<Integer,SyncIORequestLinkedList> batch = buffer.get(batchId); //get batch
            Set<Integer> appListIds = batch.keySet();
            for(Integer appListId : appListIds){
                SyncIORequestLinkedList appList = batch.get(appListId);
                numRequests += appList.getNumberOfRequests();
            }
        }
        return numRequests;
    }

    synchronized public int getNumberOfRequestsInBuffer(){
       return getNumberOfRequestsInBuffer(this.readylists);
    }


    public static int getNumberOfBytesInBuffer(Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> buffer){
        if(buffer == null){
            return 0;
        }

        int numBytes = 0;
        Set<Integer> batchesIds = buffer.keySet();
        for(Integer batchId : batchesIds){
            Hashtable<Integer,SyncIORequestLinkedList> batch = buffer.get(batchId); //get batch
            Set<Integer> appListIds = batch.keySet();
            for(Integer appListId : appListIds){
                SyncIORequestLinkedList appList = batch.get(appListId);
                numBytes += appList.getNumberOfBytes();
            }
        }
        return numBytes;
    }

    synchronized public int getNumberOfBytesInBuffer(){
        return getNumberOfBytesInBuffer(this.readylists);
    }

    public static void clear(Hashtable<Integer,Hashtable<Integer,SyncIORequestLinkedList>> buffer){
        Hashtable<Integer,SyncIORequestLinkedList> currBatch;
        SyncIORequestLinkedList currApp;
        Set<Integer> batchesIds = buffer.keySet();
        for(Integer batchId: batchesIds){
            currBatch = buffer.get(batchId);
            Set<Integer> appIds = currBatch.keySet();
            for(Integer appId:appIds){
                currApp = currBatch.get(appId);
                currApp.clearList();
            }
            currBatch.clear();
        }
        buffer.clear();
    }

    synchronized public void clear(){
        clear(this.readylists);
    }

}
