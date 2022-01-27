package StorageManager.data_structures;

import StorageManager.SettingsController;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SyncIORequestLinkedList {
	private int linkedListId;
	private LinkedList<IORequest> requests;
	
	public SyncIORequestLinkedList(int linkedListId) {
		this.linkedListId = linkedListId;
		requests = new LinkedList<IORequest>();
	}
	
	public int getLinkedListId() {
		return linkedListId;
	}
	
	public int getSize() {
		return requests.size();
	}
	
	
	public synchronized void add(IORequest req) {
		if(req!=null) {
			requests.add(req);
			if(requests.size() == 1) {
				notifyAll();
			}
		}
	}

	public synchronized void add(SyncIORequestLinkedList li){
		if(li!=null &&li.getSize()>0) {
			requests.addAll(li.requests);
		}
	}


	/**
	 * @return Retrives and removes the head of the linked list. waits in case of no elements in list.
	 * @throws InterruptedException
	 */
	public synchronized IORequest take() throws InterruptedException {

		IORequest req = null;
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

	public synchronized void printIds() {
		System.out.print("Printing all IO Requests appIds in linkedlist ");
		System.out.printf("0x%02X: ", this.linkedListId);
		for(IORequest req : requests) {
			System.out.printf("0x%02X ",req.getAppId());
		}
		System.out.print("\n");
	}
	
}
