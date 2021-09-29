package server.data_structures;

import server.enumerators.TIMESTAMP_NAME;

public class TimeStamp {
	private TIMESTAMP_NAME name;
	private long timeStamp;
	
	public TimeStamp(TIMESTAMP_NAME name, long timeStamp) {
		this.name = name;
		this.timeStamp = timeStamp;
	}
	
	public TIMESTAMP_NAME getName() {
		return this.name;
	}
	
	public long getTimeStamp() {
		return this.timeStamp;
	}
}
