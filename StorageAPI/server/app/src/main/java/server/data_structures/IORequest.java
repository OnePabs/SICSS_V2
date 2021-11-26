package server.data_structures;

import java.util.*;
import server.enumerators.TIMESTAMP_NAME;

public class IORequest {
	private long requestId;
	private byte[] content;
	private LinkedList<TimeStamp> timeStamps;
	
	public IORequest(long requestId, byte[] content) {
		this.requestId = requestId;
		this.content = content;
		this.timeStamps = new LinkedList<TimeStamp>();
	}
	
	//Getters
	public byte getBatchId() {
		return content[0];
	}
	
	public byte getAppId() {
		return content[1];
	}
	
	public byte getBatchCompleteByte() {
		return content[2];
	}
	
	public byte getAppCompleteByte() {
		return content[3];
	}
	
	public byte[] getContent() {
		return Arrays.copyOf(content,content.length);
	}
	
	public LinkedList<TimeStamp> getTimeStamps(){
		return this.timeStamps;
	}

	public long getRequestId(){
		return this.requestId;
	}
	
	
	//Add time stamp
	public void addTimeStamp(TIMESTAMP_NAME name, long timeStamp)
	{
		this.timeStamps.add(new TimeStamp(requestId,name,timeStamp));
	}
	
	public void addTimeStamp(TIMESTAMP_NAME name) {
		addTimeStamp(name,System.nanoTime());
	}
	
	public TimeStamp getTimeStamp(TIMESTAMP_NAME name){
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
