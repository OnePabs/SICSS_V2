package common;

import java.util.LinkedList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class PerformanceMetrics {

    public static double getAverateInterTime(LinkedList<MeasurementEntry> measurements, String timestampName){
        MeasurementEntry prev = null;
        long sum = (long)0;
        boolean isFirst = true;

        int numMeasurements=0;
        for(int i=0; i<measurements.size(); i++){
            if(timestampName.equals(measurements.get(i).timestampName)){
                if(!isFirst){
                    sum += (measurements.get(i).timestamp - prev.timestamp);
                }
                prev = measurements.get(i);
                if(isFirst){
                    isFirst=false;
                }
                numMeasurements++;
            }
        }

        //System.out.println("sum: " + String.valueOf(sum));
        //System.out.println("number of measurements: " + String.valueOf(numMeasurements));
        double average = sum / ((double)numMeasurements);
        //System.out.println("Average: " + String.valueOf(average));
        //System.out.println();
        return average;
    }

    public static double getRate(LinkedList<MeasurementEntry> measurements, String timestampName, int conversionFactor){
        double average = getAverateInterTime(measurements, timestampName);
        return conversionFactor/average;
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

    public static double getStorageApiArrivalRate(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        return getRate(measurements, "STORAGE_API_ENTRY",conversionFactor);
    }

    public static double getStorageApiServiceTime(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double entrySortingTime = getAverageDifference(measurements, "STORAGE_API_ENTRY", "ENTRY_LIST_ENTRY");
        //System.out.println("sorting time: " + entrySortingTime);
        double programmedDelay =  getAverageDifference(measurements, "ENTRY_LIST_EXIT", "READY_LIST_ENTRY");
        //System.out.println("programmed delay: " + programmedDelay);
        double transferTime = getAverageDifference(measurements, "TRANSMITTER_ENTRY", "TRANSMITTER_EXIT");
        //System.out.println("transferTime: " + transferTime);
        double total = entrySortingTime + programmedDelay + transferTime;
        //System.out.println("Storage API Service time before conversion: " + total);
        total = total / ((double)conversionFactor);
        //System.out.println("Storage API Service time after conversion: " + total);
        return total;
    }

    public static double getStorageApiResidenceTime(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double res = getAverageDifference(measurements, "STORAGE_API_ENTRY","TRANSMITTER_EXIT");
        res = res / conversionFactor;
        return res;
    }

    public static double getStorageApiThroughput(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        return getRate(measurements, "TRANSMITTER_EXIT",conversionFactor);
    }

    public static double getStorageApiUtilization(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double throughput = getStorageApiThroughput(measurements,conversionFactor);
        double serviceTime = getStorageApiServiceTime(measurements,conversionFactor);
        return getUtilization(throughput,serviceTime);
    }

    public static double getStorageApiJobsInSystem(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double arrivalRate = getStorageApiArrivalRate(measurements, conversionFactor);
        double timeInSystem = getAverageDifference(measurements, "STORAGE_API_ENTRY","TRANSMITTER_EXIT");
        timeInSystem = timeInSystem / conversionFactor;
        return getJobsInSystem(arrivalRate,timeInSystem);
    }

    public static double getStorageManagerArrivalRate(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        return getRate(measurements, "ENTRY", conversionFactor);
    }

    public static double getStorageManagerServiceTime(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double serv = getAverageDifference(measurements, "QueueExitTime", "EXIT");
        serv = serv / conversionFactor;
        return serv;
    }

    public static double getStorageManagerResidenceTime(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double res = getAverageDifference(measurements, "ENTRY", "EXIT");
        res = res / conversionFactor;
        return res;
    }

    public static double getStorageManagerThroughput(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        return getRate(measurements, "EXIT", conversionFactor);
    }

    public static double getStorageManagerUtilization(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double throughput = getStorageManagerThroughput(measurements, conversionFactor);
        double serviceTime = getStorageManagerServiceTime(measurements, conversionFactor);
        return getUtilization(throughput,serviceTime);
    }

    public static double getStorageManagerJobsInSystem(LinkedList<MeasurementEntry> measurements, int conversionFactor){
        double arrivalRate = getStorageManagerArrivalRate(measurements, conversionFactor);
        double totalTimeInSystem = getStorageManagerServiceTime(measurements, conversionFactor);
        return getJobsInSystem(arrivalRate,totalTimeInSystem);
    }


    public static double getUtilization(double throughput, double serviceTime){
        return throughput*serviceTime;
    }

    public static double getJobsInSystem(double arrivalRate, double totalTimeInSystem){
        return arrivalRate*totalTimeInSystem;
    }


    public static void writeStorageApiPerformanceMetrics(String measurementsFileName, String performanceResultsFileName){
        try {
            LinkedList<MeasurementEntry> measurements = new LinkedList<MeasurementEntry>();

            //read measurements
            File myObj = new File(measurementsFileName);
            Scanner myReader = new Scanner(myObj);
            boolean isFirst = true;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(!isFirst && data != null && !data.isBlank()){
                    String[] values = data.split(",");
                    //System.out.println(values[0]);
                    int id = Integer.valueOf(values[0]);
                    //System.out.println(values[1]);
                    String timestampName = values[1];
                    //System.out.println(values[2]);
                    long timeStamp = Long.valueOf(values[2]);
                    MeasurementEntry measurement = new MeasurementEntry(id,timestampName,timeStamp);
                    measurements.add(measurement);
                    //System.out.println();
                }else{
                    isFirst = false;
                }
            }
            myReader.close();

            measurements.sort(new MeasurementsComparator());


            //write performance metrics
            JsonToCsv.writeStorageApiPerformanceMetrics(measurements,performanceResultsFileName);
            //JsonToCsv.writeStorageManagerPerformanceMetrics(measurements,performanceResultsFileName);


        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void writeStorageManagerPerformanceMetrics(String measurementsFileName, String performanceResultsFileName){
        try {
            LinkedList<MeasurementEntry> measurements = new LinkedList<MeasurementEntry>();

            //read measurements
            File myObj = new File(measurementsFileName);
            Scanner myReader = new Scanner(myObj);
            boolean isFirst = true;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(!isFirst && data != null && !data.isBlank()){
                    String[] values = data.split(",");
                    int id = Integer.valueOf(values[0]);
                    String timestampName = values[1];
                    long timeStamp = Long.valueOf(values[2]);
                    MeasurementEntry measurement = new MeasurementEntry(id,timestampName,timeStamp);
                    measurements.add(measurement);
                }else{
                    isFirst = false;
                }
            }
            myReader.close();

            measurements.sort(new MeasurementsComparator());


            //write performance metrics
            //JsonToCsv.writeStorageApiPerformanceMetrics(measurements,performanceResultsFileName);
            JsonToCsv.writeStorageManagerPerformanceMetrics(measurements,performanceResultsFileName);

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}