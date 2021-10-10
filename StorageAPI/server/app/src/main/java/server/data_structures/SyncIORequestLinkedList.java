package server.data_structures;

import server.enumerators.PROGRAM_STATE;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SyncIORequestLinkedList {
	private byte linkedListId;
	private LinkedList<IORequest> requests;
	private StateController stateController;
	
	public SyncIORequestLinkedList(byte linkedListId) {
		this.linkedListId = linkedListId;
		requests = new LinkedList<IORequest>();
	}

	public void setStateController(StateController stateController){
		this.stateController = stateController;
	}
	
	
	public byte getLinkedListId() {
		return linkedListId;
	}
	
	public int getSize() {
		return requests.size();
	}
	
	
	public synchronized void addIORequest(IORequest req) {
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING) {
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
	
	
	public synchronized LinkedList<IORequest> getAndRemoveAllRequests(){
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING) {
			LinkedList<IORequest> tempRequests = requests;
			requests = new LinkedList<IORequest>();
			return tempRequests;
		}
		return null;
	}
	
	
	/**
	 * @param appId
	 * @return all requests with appId that are in this linked list
	 */
	public synchronized LinkedList<IORequest> getAndRemoveAllRequestsWithAppId(byte appId){
		if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING) {
			LinkedList<IORequest> appRequests = new LinkedList<IORequest>();

			int maxIndex = requests.size();
			int idx = 0;
			while (idx < maxIndex) {
				IORequest req = requests.get(idx);
				if (req.getAppId() == appId) {
					appRequests.add(req);
					requests.remove(idx);
					maxIndex--; //one less element in list
				} else {
					idx++;//check the next request
				}
			}
			return appRequests;
		}
		return null;
	}
	
	
	public void clearList() {
		if(stateController.getCurrentState() == PROGRAM_STATE.STOPPED) {
			requests = new LinkedList<IORequest>();
		}
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
