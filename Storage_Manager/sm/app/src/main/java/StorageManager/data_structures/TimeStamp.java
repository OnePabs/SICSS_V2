package StorageManager;

public class TimeStamp {
    private int requestId;
    private String name;
    private long timeStamp;

    public TimeStamp(int requestId, String name, long timeStamp) {
        this.requestId = requestId;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    //GETTERS
    public long getRequestId(){return this.requestId;}
    public String getName() {
        return this.name;
    }
    public long getTimeStamp() {
        return this.timeStamp;
    }
}
