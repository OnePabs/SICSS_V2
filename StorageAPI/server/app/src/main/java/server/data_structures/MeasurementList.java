package server.data_structures;

import java.util.LinkedList;

public class MeasurementList {
    private String measurementName;
    private LinkedList<Long> measurements;

    public MeasurementList(String measurementName){
        this.measurementName = measurementName;
        this.measurements = new LinkedList<Long>();
    }

    public String getMeasurementName(){
        return measurementName;
    }

    public void addMeasurement(Long measurement){
        measurements.add(measurement);
    }

    public Long[] getMeasurements(){
        return measurements.toArray(new Long[measurements.size()]);
    }

    public void clearMeasurements(){
        measurements = new LinkedList<Long>();
    }
}
