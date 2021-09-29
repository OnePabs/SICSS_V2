package server.data_structures;

import java.util.*;
import server.enumerators.TIMESTAMP_NAME;

public class IORequest {
	
	public byte[] content;
	public LinkedList<TimeStamp> timeStamps;
	
	public IORequest(byte[] content) {
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
		return content;
	}
	
	public LinkedList<TimeStamp> getTimeStamps(){
		return this.timeStamps;
	}
	
	
	//Add time stamp
	public void addTimeStamp(TIMESTAMP_NAME name, long timeStamp)
	{
		this.timeStamps.add(new TimeStamp(name,timeStamp));
	}
	
	public void addTimeStamp(TIMESTAMP_NAME name) {
		addTimeStamp(name,System.nanoTime());
	}
	
	
	//Completness of batch/application
	public boolean isBatchComplete() {
		if(getBatchCompleteByte() == (byte)0)
			return true;
		return false;
	}
	
	public boolean isApplicationComplete() {
		if(getAppCompleteByte() == (byte)0)
			return true;
		return false;
	}
}
