package common;

public class MeasurementEntry {
    public int id;
    public String timestampName;
    public long timestamp;

    public MeasurementEntry(){}
    public MeasurementEntry(int id, String timestampName, long timestamp){
        this.id = id;
        this.timestampName = timestampName;
        this.timestamp = timestamp;
    }
}