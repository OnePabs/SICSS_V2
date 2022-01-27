package StorageManager.data_structures;

import StorageManager.SettingsController;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SyncStringLinkedList {
    private int linkedListId;
    private LinkedList<String> requests;

    public SyncStringLinkedList(int linkedListId) {
        this.linkedListId = linkedListId;
        requests = new LinkedList<String>();
    }

    public int getLinkedListId() {
        return linkedListId;
    }

    public int getSize() {
        return requests.size();
    }


    public synchronized void add(String req) {
        if(req!=null) {
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
                req = requests.remove();
            } catch (NoSuchElementException e) {
                req = null;
                wait();
            }
        } while (req == null);
        return req;
    }

    public void clear() {
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
