package StorageManager.data_structures;

import java.util.*;
import StorageManager.*;

public class IORequest {
	private byte[] content;
	private LinkedList<TimeStamp> timeStamps;
	
	public IORequest(byte[] content) {
		this.content = content;
		this.timeStamps = new LinkedList<TimeStamp>();
	}
	
	//Getters
	public int getRequestId(){
		//uses big endian to parse first two bytes to get the request Id
		int least_significant = content[1] & 0xFF;
		int most_significant = content[0] & 0xFF;

		//System.out.println("first byte: " + most_significant);
		//System.out.println("second byte: " + least_significant);
		int requestId = most_significant*16*16 + least_significant;
		//System.out.println("requestID: " + requestId);
		return requestId;
	}

	public int getAppId() {
		return content[2] & 0xFF;
	}

	public int getBatchId() {
		return content[3] & 0xFF;
	}
	
	public int getBatchCompleteByte() {
		return content[4] & 0xFF;
	}
	
	public int getAppCompleteByte() {
		return content[5] & 0xFF;
	}
	
	public byte[] getContent() {
		return Arrays.copyOf(content,content.length);
	}
	
	public LinkedList<TimeStamp> getTimeStamps(){
		return this.timeStamps;
	}
	
	
	//Add time stamp
	public void addTimeStamp(String name, long timeStamp)
	{
		this.timeStamps.add(new TimeStamp(this.getRequestId(),name,timeStamp));
	}
	
	public void addTimeStamp(String name) {
		addTimeStamp(name,System.nanoTime());
	}
	
	public TimeStamp getTimeStamp(String name){
		for(TimeStamp tp : timeStamps){
			if(tp.getName().equals(name)){
				return tp;
			}
		}
		return null;
	}


	//Completness of batch/application
	public boolean isBatchComplete() {
		if(getBatchCompleteByte() == (byte)1)
			return true;
		return false;
	}
	
	public boolean isApplicationComplete() {
		if(getAppCompleteByte() == (byte)1)
			return true;
		return false;
	}
}
