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
	
	public SyncIORequestLinkedList(byte linkedListId, StateController stateController) {
		this.linkedListId = linkedListId;
		requests = new LinkedList<IORequest>();
		this.stateController = stateController;
	}
	
	public byte getLinkedListId() {
		return linkedListId;
	}
	
	public int getSize() {
		return requests.size();
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
