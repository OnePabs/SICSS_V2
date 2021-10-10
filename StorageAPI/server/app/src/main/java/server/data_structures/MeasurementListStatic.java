package server.data_structures;

public class MeasurementListStatic {
    public String measurementName;
    public Long[] measurements;

    public MeasurementListStatic(MeasurementList measurementList){
        this.measurementName = measurementList.getMeasurementName();
        measurements = measurementList.getMeasurements();
    }
}
