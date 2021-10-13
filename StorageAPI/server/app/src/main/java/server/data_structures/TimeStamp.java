package server.data_structures;

import server.enumerators.TIMESTAMP_NAME;

public class TimeStamp {
	private long requestId;
	private TIMESTAMP_NAME name;
	private long timeStamp;
	
	public TimeStamp(long requestId, TIMESTAMP_NAME name, long timeStamp) {
		this.requestId = requestId;
		this.name = name;
		this.timeStamp = timeStamp;
	}

	//GETTERS
	public long getRequestId(){return this.requestId;}
	public TIMESTAMP_NAME getName() {
		return this.name;
	}
	public long getTimeStamp() {
		return this.timeStamp;
	}
}
