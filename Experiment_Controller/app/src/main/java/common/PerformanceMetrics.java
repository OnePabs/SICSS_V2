package common;

import java.util.LinkedList;

public class PerformanceMetrics {

    public static double getAverateInterTime(LinkedList<MeasurementEntry> measurements, String timestampName){
        MeasurementEntry prev = null;
        long sum = (long)0;

        for(int i=0; i<measurements.size(); i++){
            if(timestampName.equals(measurements.get(i).timestampName)){
                if(i!=0){
                    sum += (measurements.get(i).timestamp - prev.timestamp);
                }
                prev = measurements.get(i);
            }
        }

        double average = sum / ((double)measurements.size());
        return average;
    }

    public static double getRate(LinkedList<MeasurementEntry> measurements, String timestampName){
        double average = getAverateInterTime(measurements, timestampName);
        return 1/average;
    }

    public static double getAverageDifference(LinkedList<MeasurementEntry> measurements, String startStampName, String endStampName){
        MeasurementEntry currentStart = null;
        long sum = (long)0;
        int numDifferences = 0;

        for(int i=0; i<measurements.size(); i++){
            if(measurements.get(i).timestampName.equals(startStampName)){
                currentStart = measurements.get(i);
            }else if(measurements.get(i).timestampName.equals(endStampName)){
                sum += (measurements.get(i).timestamp - currentStart.timestamp);
                numDifferences++;
            }
        }

        double average = sum/((double)numDifferences);
        return average;
    }

    public static double getStorageApiArrivalRate(LinkedList<MeasurementEntry> measurements){
        return getRate(measurements, "STORAGE_API_ENTRY");
    }

    public static double getStorageApiServiceTime(LinkedList<MeasurementEntry> measurements){
        double entrySortingTime = getAverageDifference(measurements, "STORAGE_API_ENTRY", "ENTRY_LIST_ENTRY,");
        double programmedDelay =  getAverageDifference(measurements, "ENTRY_LIST_EXIT", "READY_LIST_ENTRY");
        double transferTime = getAverageDifference(measurements, "TRANSMITTER_ENTRY", "TRANSMITTER_EXIT");
        double total = entrySortingTime + programmedDelay + transferTime;
        return total;
    }

    public static double getStorageApiResidenceTime(LinkedList<MeasurementEntry> measurements){
        return getAverageDifference(measurements, "STORAGE_API_ENTRY","TRANSMITTER_EXIT");
    }

    public static double getStorageApiThroughput(LinkedList<MeasurementEntry> measurements){
        return getRate(measurements, "TRANSMITTER_EXIT");
    }

    public static double getStorageApiUtilization(LinkedList<MeasurementEntry> measurements){
        double throughput = getStorageApiThroughput(measurements);
        double serviceTime = getStorageApiServiceTime(measurements);
        return getUtilization(throughput,serviceTime);
    }

    public static double getStorageApiJobsInSystem(LinkedList<MeasurementEntry> measurements){
        double arrivalRate = getStorageApiArrivalRate(measurements);
        double timeInSystem = getAverageDifference(measurements, "STORAGE_API_ENTRY","TRANSMITTER_EXIT");
        return getJobsInSystem(arrivalRate,timeInSystem);
    }

    public static double getStorageManagerArrivalRate(LinkedList<MeasurementEntry> measurements){
        return getRate(measurements, "ENTRY");
    }

    public static double getStorageManagerServiceTime(LinkedList<MeasurementEntry> measurements){
        return getAverageDifference(measurements, "ENTRY", "EXIT");
    }

    public static double getStorageManagerThroughput(LinkedList<MeasurementEntry> measurements){
        return getRate(measurements, "EXIT");
    }

    public static double getStorageManagerUtilization(LinkedList<MeasurementEntry> measurements){
        double throughput = getStorageManagerThroughput(measurements);
        double serviceTime = getStorageManagerServiceTime(measurements);
        return getUtilization(throughput,serviceTime);
    }

    public static double getStorageManagerJobsInSystem(LinkedList<MeasurementEntry> measurements){
        double arrivalRate = getStorageManagerArrivalRate(measurements);
        double totalTimeInSystem = getStorageManagerServiceTime(measurements);
        return getJobsInSystem(arrivalRate,totalTimeInSystem);
    }


    public static double getUtilization(double throughput, double serviceTime){
        return throughput*serviceTime;
    }

    public static double getJobsInSystem(double arrivalRate, double totalTimeInSystem){
        return arrivalRate*totalTimeInSystem;
    }


}