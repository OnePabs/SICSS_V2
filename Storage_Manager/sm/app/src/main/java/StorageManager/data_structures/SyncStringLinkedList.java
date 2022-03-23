package StorageManager.data_structures;

import StorageManager.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SyncStringLinkedList {
    private int linkedListId;
    private LinkedList<String> requests;
    private StateController stateController;

    public SyncStringLinkedList(int linkedListId, StateController stateController) {
        this.linkedListId = linkedListId;
        this.stateController = stateController;
        requests = new LinkedList<String>();
    }

    public int getLinkedListId() {
        return linkedListId;
    }

    public int getSize() {
        return requests.size();
    }


    public synchronized void add(String req) {
        if(req!=null && stateController.isStateRunning()) {
            requests.add(req);
            if(requests.size() == 1) {
                notifyAll();
            }
        }
    }



    /**
     * @return Retrives and removes the head of the linked list. waits in case of no elements in list.
     * @throws InterruptedException
     */
    public synchronized String take() throws InterruptedException {

        String req = null;
        do {
            try {
                if(stateController.isStateRunning()){
                    req = requests.remove();
                }else{
                    return null;
                }
            } catch (NoSuchElementException e) {
                if(stateController.isStateRunning()){
                    req = null;
                    wait();
                }
            }
        } while (req == null);
        return req;
    }

    public synchronized void wakeAll(){
		notifyAll();
	}

    public synchronized void clear() {
        requests.clear();
    }

    public synchronized void print() {
        System.out.print("Printing String linked list ");
        System.out.println(this.linkedListId);
        for(String req : requests) {
            System.out.println(req);
            System.out.print("\n");
        }
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");
    }

}
