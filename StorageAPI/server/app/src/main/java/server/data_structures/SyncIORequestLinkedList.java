package server.data_structures;

import server.enumerators.PROGRAM_STATE;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.util.*;

public class SyncIORequestLinkedList implements Iterable{
	private int linkedListId;
	private LinkedList<IORequest> requests;
	private StateController stateController;
	
	public SyncIORequestLinkedList(int linkedListId, StateController stateController) {
		this.linkedListId = linkedListId;
		requests = new LinkedList<IORequest>();
		this.stateController = stateController;
	}
	
	public int getLinkedListId() {
		return linkedListId;
	}
	
	public synchronized int getSize() {
		return requests.size();
	}

	public synchronized int getNumberOfRequests(){
		if(requests == null){
			return 0;
		}
		return requests.size();
	}

	public synchronized int getNumberOfBytes(){
		if(requests == null){
			return 0;
		}

		int numBytes = 0;
		for(IORequest req: requests){
			numBytes += req.getSize();
		}
		return numBytes;
	}

	public synchronized IORequest[] getRequests(){
		if(requests == null){
			return null;
		}
		return requests.toArray(new IORequest[requests.size()]);
	}
	
	
	public synchronized void add(IORequest req) {
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING && req!=null) {
			requests.add(req);
			if(requests.size() == 1) {
				notifyAll();
			}
		}
	}

	public synchronized void add(SyncIORequestLinkedList li){
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING && li!=null &&li.getSize()>0) {
			requests.addAll(li.requests);
		}
	}


	/**
	 * @return Retrives and removes the head of the linked list. waits in case of no elements in list.
	 * @throws InterruptedException
	 */
	public synchronized IORequest take() throws InterruptedException {
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING) {
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
		return null;
	}

	public void clearList() {
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

	public IORequest[] getAsArray(){
		IORequest[] requestArray = new IORequest[requests.size()];
		requests.toArray(requestArray);
		return requestArray;
	}


	public Iterator<IORequest> iterator() {
        return this.requests.iterator();
    }

}
