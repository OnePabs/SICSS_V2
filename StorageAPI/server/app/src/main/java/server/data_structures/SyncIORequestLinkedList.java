package server.data_structures;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SyncIORequestLinkedList {
	private byte linkedListId;
	private LinkedList<IORequest> requests;
	public boolean allowAdd = false;
	
	
	public SyncIORequestLinkedList(byte linkedListId) {
		this.linkedListId = linkedListId;
		requests = new LinkedList<IORequest>();
	}
	
	
	public byte getLinkedListId() {
		return linkedListId;
	}
	
	public int getSize() {
		return requests.size();
	}
	
	
	public synchronized void addIORequest(IORequest req) {
		if(allowAdd) {
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
		IORequest req = null;
		do {
			try {
				req = requests.remove();
			}catch(NoSuchElementException e) {
				req = null;
				wait();
			}
		}while(req == null);
		return req;
	}
	
	
	public synchronized LinkedList<IORequest> getAndRemoveAllRequests(){
		LinkedList<IORequest> tempRequests = requests;
		requests = new LinkedList<IORequest>();
		return tempRequests;
	}
	
	
	/**
	 * @param appId
	 * @return all requests with appId that are in this linked list
	 */
	public synchronized LinkedList<IORequest> getAndRemoveAllRequestsWithAppId(byte appId){
		LinkedList<IORequest> appRequests = new LinkedList<IORequest>();
		
		int maxIndex = requests.size();
		int idx = 0;
		while(idx < maxIndex) {
			IORequest req = requests.get(idx);
			if(req.getAppId() == appId) {
				appRequests.add(req);
				requests.remove(idx);
				maxIndex--; //one less element in list
			}else {
				idx++;//check the next request
			}
		}
		return appRequests;
	}
	
	
	public void clearList() {
		requests = new LinkedList<IORequest>();
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
