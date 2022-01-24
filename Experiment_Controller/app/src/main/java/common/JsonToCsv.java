package common;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

import common.TIMESTAMP_NAME;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JsonToCsv {

	private boolean isVerbose;
	
	public JsonToCsv(boolean isVerbose) throws Exception {
        //set isVerbose
        this.isVerbose = isVerbose;
	}

	public static LinkedList<MeasurementEntry> getAndStoreMeasurements(String jsonArrayStr, String resultsFolderpath, String moduleName) throws Exception{
		LinkedList<MeasurementEntry> measurements = getMeasurements(jsonArrayStr);
		String basepath = resultsFolderpath + "/measurements/" + moduleName + ".txt";
		writeMeasurementsToFile(basepath, measurements);
        return measurements;
	}

	public static LinkedList<MeasurementEntry> getMeasurements(String jsonArrayStr) throws Exception{
		JSONArray mesJsonArray = parseJsonArrayStr(jsonArrayStr);
		LinkedList<MeasurementEntry> measurements = mapJsonMeasurementsToMeasurementEntries(mesJsonArray);
		if(measurements==null || measurements.isEmpty()) {
			throw new Exception("JSON to CSV received empty measurements");
		}
		//Collections.sort(measurements, new TimeStampComparator());
		return measurements;
	}


	public static boolean writeMeasurementsToFile(String filename, LinkedList<MeasurementEntry> measurements) {
		//write measurements to a file in csv format
		
		if( measurements == null || measurements.isEmpty()) {
			return false;
		}
		
		try {
	        FileWriter myWriter = new FileWriter(filename);
	        
	        //write headers
	        myWriter.write("Request ID");
	        myWriter.write(",");
	        myWriter.write("Time Stamp Name");
	        myWriter.write(",");
	        myWriter.write("Time Stamp");
	        myWriter.write("\n");
	        
	        
	        //write measurements
	        for(MeasurementEntry me: measurements) {
	        	myWriter.write(String.valueOf(me.id));
	        	myWriter.write(",");
	        	myWriter.write(me.timestampName);
	        	myWriter.write(",");
	        	myWriter.write(String.valueOf(me.timestamp));
	        	myWriter.write("\n");
	        }
	        
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    return true;
	}



    public static void writeStorageApiPerformanceMetrics( LinkedList<MeasurementEntry> measurements,String resultsBasePath){
        try{
            String path = resultsBasePath + "/performance_metrics/" + "strgapi" + ".txt";
            //create file
            Path basic_calculations_path = Paths.get(path);
            if (Files.notExists(basic_calculations_path)) {
                FileWriter myHeaderWriter = new FileWriter(path);

                //headers
                myHeaderWriter.write("Arrival Rate (requests per second)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Service time (milliseconds)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Residence time (milliseconds)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Throughput (requests per second)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Utilization");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Number of jobs in the system");
                myHeaderWriter.write("\n");
                myHeaderWriter.close();
            }



            //write data
            FileWriter myWriter = new FileWriter(path,true);
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiArrivalRate(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiServiceTime(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiResidenceTime(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiThroughput(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiUtilization(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageApiJobsInSystem(measurements)));

            myWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeStorageManagerPerformanceMetrics(LinkedList<MeasurementEntry> measurements,String resultsBasePath){
        try{
            String path = resultsBasePath + "/performance_metrics/" + "strgMngr" + ".txt";
            //create file
            Path basic_calculations_path = Paths.get(path);
            if (Files.notExists(basic_calculations_path)) {
                FileWriter myHeaderWriter = new FileWriter(path);

                //headers
                myHeaderWriter.write("Arrival Rate (requests per second)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Service time (milliseconds)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Residence time (milliseconds)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Throughput (requests per second)");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Utilization");
                myHeaderWriter.write(",");
                myHeaderWriter.write("Average Number of jobs in the system");
                myHeaderWriter.write("\n");
                myHeaderWriter.close();
            }



            //write data
            FileWriter myWriter = new FileWriter(path,true);
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerArrivalRate(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerServiceTime(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerServiceTime(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerThroughput(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerUtilization(measurements)));
            myWriter.write(",");
            myWriter.write(String.valueOf(PerformanceMetrics.getStorageManagerJobsInSystem(measurements)));

            myWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
	public static void writeModulePerformanceMetrics(
			String resultsBasePath,
			String moduleName,
			int numExperiments,
			long[] experimentRuntimes,
			LinkedList<Double> arrivalRates,
			LinkedList<Double> serviceTimes,
			LinkedList<Double> throughputs,
			LinkedList<Double> utilizations,
			LinkedList<Double> averageNumberOfjobs
	){
		try{
			//create file
			Path basic_calculations_path = Paths.get(resultsBasePath + moduleName + ".txt");
			if (Files.notExists(basic_calculations_path)) {
				FileWriter myHeaderWriter = new FileWriter(resultsBasePath+ moduleName + ".txt");

				//headers
				myHeaderWriter.write("Experiment Runtime (minutes)");
				myHeaderWriter.write(",");
				myHeaderWriter.write(moduleName + "Arrival Rate (requests per second)");
				myHeaderWriter.write(",");
				myHeaderWriter.write(moduleName + "Storage API Residence time (milliseconds)");
				myHeaderWriter.write(",");
				myHeaderWriter.write(moduleName + "Throughput (requests per second)");
				myHeaderWriter.write(",");
				myHeaderWriter.write(moduleName + "Utilization");
				myHeaderWriter.write(",");
				myHeaderWriter.write(moduleName + "Average Number of jobs in the system");
				myHeaderWriter.write("\n");
				myHeaderWriter.close();
			}

			//write data
			FileWriter myWriter = new FileWriter(resultsBasePath + moduleName + ".txt",true);
			for(int i=0; i<numExperiments; i++){
				myWriter.write(String.valueOf(experimentRuntimes[i]));
				myWriter.write(",");
				myWriter.write(String.valueOf(arrivalRates.get(i)));
				myWriter.write(",");
				myWriter.write(String.valueOf(serviceTimes.get(i)));
				myWriter.write(",");
				myWriter.write(String.valueOf(throughputs.get(i)));
				myWriter.write(",");
				myWriter.write(String.valueOf(utilizations.get(i)));
				myWriter.write(",");
				myWriter.write(String.valueOf(averageNumberOfjobs.get(i)));
				myWriter.write("\n");
			}
			myWriter.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
     */
    /*
	public static double getAvrgInterTime(String timeStampName, <LinkedList<MeasurementEntry> measurements){
		MeasurementEntry current;
		long sum;

		for(int i=0; i<measurements.size();i++){
			if(i!=0){
				sum += measurements.get(i).timestamp - current.timestamp;
			}
			current = measurements.get(i);
		}
		return sum/((double)measurements.size());
	}

	public static double getRate(String timeStampName, <LinkedList<MeasurementEntry> measurements){
		return 1/getAvrgInterTime(timeStampName,measurements);
	}
}

	/*
	public boolean writeBasicCalculationsToFile(double serviceRate, String resultsBasePath, boolean includeMM1Expected) {
		//writes basic general calculations from the measurements
		//headers are: 
		//Experiment Runtime, Arrival Rate, Residence Time, Throughput, MM1 Expected Residence Time, Average Number of jobs in the system, MM1 Expected Number of jobs in the system, Percentage Difference between observed and MM1 expected Calculations   
		
		if( measurements == null || measurements.isEmpty() || measurements.size() < 2) {
			return false;
		}
		
	    try {
	    	Path basic_calculations_path = Paths.get(resultsBasePath + "basic_calculations.txt");
	    	if (Files.notExists(basic_calculations_path)) {
		        FileWriter myHeaderWriter = new FileWriter(resultsBasePath + "basic_calculations.txt");
		        
		        //write headers
		        myHeaderWriter.write("Experiment Runtime (minutes)");
		        myHeaderWriter.write(",");
		        myHeaderWriter.write("Arrival Rate (requests/second)");
		        myHeaderWriter.write(",");
		        myHeaderWriter.write("Throughput (requests/second)");
		        myHeaderWriter.write(",");
		        myHeaderWriter.write("Average Residence Time (milliseconds)");
		        myHeaderWriter.write(",");
		        if(includeMM1Expected) {
		        	myHeaderWriter.write("MM1 Expected Residence Time (milliseconds)");
		        	myHeaderWriter.write(",");
		        	myHeaderWriter.write("Percentage difference between Measured Residence time and Expected residence time of an MM1 queue (%)");
		        	myHeaderWriter.write(",");
		        }
		        myHeaderWriter.write("Average Number of jobs in the system");
		        if(includeMM1Expected) {
		        	myHeaderWriter.write(",");
		        	myHeaderWriter.write("MM1 Expected Number of jobs in the system");
			        myHeaderWriter.write(",");
			        myHeaderWriter.write("Percentage difference between Measured Average Number of Jobs in the system and Expected Average Number of Jobs in the system in a MM1 queue (%)");
		        }
		        myHeaderWriter.write("\n");
		        myHeaderWriter.close();
	    	}

	    	FileWriter myWriter = new FileWriter(resultsBasePath + "basic_calculations.txt",true);
	        
	        //Experiment runtime
	        long startTime = measurements.getFirst().timestamp;
	        long endTime = measurements.getLast().timestamp;
	        long runtimeNano = endTime - startTime; //runtime in nanoseconds
	        double runtime = (double)runtimeNano/1000000000; //runtime in seconds
	        runtime = runtime/60; //runtime in minutes
	        myWriter.write(String.valueOf(runtime));
	        myWriter.write(",");
	        
	       
	        
	        //Arrival rate
	        long interArrivalTimesSum = 0;
	        double avgInterArrivalTime;
	        double arrivalRate;
	        if(requests.size()<=1) {
	        	arrivalRate = 0;
	        }else {
	        	for(int i=1;i<requests.size();i++) {
	        		interArrivalTimesSum += (requests.get(i).STORAGE_API_ENTRY_time_stamp -requests.get(i-1).STORAGE_API_ENTRY_time_stamp);
	        	}
	        	avgInterArrivalTime = (double)interArrivalTimesSum/(requests.size()-1); //average inter arrival time in nanoseconds
	        	avgInterArrivalTime = avgInterArrivalTime/1000000000; //convert average inter-arrival time to seconds
	        	arrivalRate = 1/avgInterArrivalTime;
	        }
	        myWriter.write(String.valueOf(arrivalRate));
	        myWriter.write(",");
	        
	        
	        //throughput
	        long interExitTimeSum = 0;
	        double interExitTimesAvg;
	        double throughput;
	        if(requests.size()<=1) {
	        	throughput = 0;
	        }else {
	        	for(int i=1;i<requests.size();i++) {
	        		interExitTimeSum += (requests.get(i).TRANSMITTER_EXIT_time_stamp -requests.get(i-1).TRANSMITTER_EXIT_time_stamp);
	        	}
	        	interExitTimesAvg = (double)interExitTimeSum/(requests.size()-1); //average inter exit time in nanoseconds
	        	interExitTimesAvg = interExitTimesAvg/1000000000; //convert average inter-arrival time to seconds
	        	throughput = 1/interExitTimesAvg;
	        	myWriter.write(String.valueOf(throughput));
		        myWriter.write(",");
	        }
	        
	        
	        //residence time
	        long resTimeSum = 0;
	        double resTimeAvg;
	        if(requests.isEmpty()) {
	        	resTimeAvg = 0;
	        }else {
	        	for(int i=0;i<requests.size();i++) {
	        		resTimeSum += (requests.get(i).TRANSMITTER_EXIT_time_stamp - requests.get(i).STORAGE_API_ENTRY_time_stamp);
	        	}
	        	resTimeAvg = (double)resTimeSum/requests.size(); //average residence time in nanoseconds
	        	resTimeAvg = resTimeAvg/1000000; //convert residence time to milliseconds
	        }
	        myWriter.write(String.valueOf(resTimeAvg));
	        myWriter.write(",");
	        
	        //MM1 Expected Residence Time
	        //Expected response time or sojourn time is: 1/(mu-lambda), where mu is service rate and lambda is arrival rate
	        //from: https://en.wikipedia.org/wiki/M/M/1_queue
	        double expectedMM1ResidenceTime = 0;
	        if(includeMM1Expected) {
		        expectedMM1ResidenceTime = 1/(serviceRate - arrivalRate); //expected MM1 residence time in seconds
		        expectedMM1ResidenceTime = expectedMM1ResidenceTime*1000; //convert to milliseconds
		        myWriter.write(String.valueOf(expectedMM1ResidenceTime));
		        myWriter.write(",");
		        
		      //Percentage difference between Measured Residence time and Expected residence time of an MM1 queue
		      double residancePercentageDiff = Math.abs(expectedMM1ResidenceTime - resTimeAvg)*100/expectedMM1ResidenceTime;
		      myWriter.write(String.valueOf(residancePercentageDiff));
		        myWriter.write(",");
	        }
	        
	        
	        //Average Number of jobs in the system
	        double numJobsInSystemAvg = (double)resTimeSum/runtimeNano;
	        myWriter.write(String.valueOf(numJobsInSystemAvg));
	        if(includeMM1Expected) {
	        	myWriter.write(",");
	        }else {
	        	myWriter.write("\n");
	        }
	        

	        //mm1 expected number of jobs in the system
	        double rho = 0;
	        double expectedMM1numjobs = 0;
	        if(includeMM1Expected) {
	        	rho = arrivalRate/serviceRate;
	        	expectedMM1numjobs = rho/(1-rho);
	        	myWriter.write(String.valueOf(expectedMM1numjobs));
		        myWriter.write(",");
		        
		      //Percentage difference between Measured Average Number of Jobs in the system and Expected Average Number of Jobs in the system in a MM1 queue (%)
		      double rhoExp = arrivalRate*(expectedMM1ResidenceTime/1000); //expected residence time was in milliseconds so it is converted to seconds
		      expectedMM1numjobs = rhoExp/(1-rhoExp);
		      myWriter.write(String.valueOf(expectedMM1numjobs));
		      myWriter.write("\n");
	        }
	        
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    
	    return true;
	}
	
	private static void printMeasurementEntries(LinkedList<MeasurementEntry> mes) {
		System.out.println("Number of measurement entries: " + mes.size());
		for(MeasurementEntry me: mes) {
			System.out.println("Id:"+me.id+" , TimeStamp Name: " + me.timestampName.toString() +" , TimeStamp value: " + me.timestamp);
		}
	}
*/
	private static LinkedList<MeasurementEntry> mapJsonMeasurementsToMeasurementEntries(JSONArray jsonArray){
		LinkedList<MeasurementEntry> entries = new LinkedList<MeasurementEntry>();
		 Iterator i = jsonArray.iterator();
		 while(i.hasNext()) {
			 JSONObject jobj = (JSONObject)i.next();
			 MeasurementEntry me = new MeasurementEntry();
			 me.id = (int)jobj.get("requestId");
			 me.timestampName = jobj.get("TIMESTAMP_NAME").toString();
			 me.timestamp = (long)jobj.get("timeStamp");
			 entries.add(me);
		 }
		return entries;
	}


	private static JSONArray parseJsonArrayStr(String jsonArrayStr) {

		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
        try {
            //parse JSON string into JSON object {key:value,...}
        	jsonArray = (JSONArray)parser.parse(jsonArrayStr);
        }catch(ParseException pe) {
            System.out.println("Error parsing String into JSONObject. Saving measurements as string");
            jsonArray = null;
        }
        return jsonArray;
	}
    /*
	
	private static void printIORequests(LinkedList<IORequest> requests) {
		if(requests == null || requests.isEmpty()) {
			return;
		}
		for(IORequest req : requests) {
			System.out.println();
			System.out.println("----");
			System.out.println("Printing measurements grouped into IORequest");
			System.out.println(
					"Request Id: " + req.id + "," 
					+ "STORAGE_API_ENTRY: " + req.STORAGE_API_ENTRY_time_stamp + "," 
					+ "ENTRY_LIST_ENTRY: " + req.ENTRY_LIST_ENTRY_time_stamp + "," 
					+ "ENTRY_LIST_EXIT: " + req.ENTRY_LIST_EXIT_time_stamp + "," 
					+ "SERVICE_TIME_START: " + req.SERVICE_TIME_START_time_stamp + "," 
					+ "SERVICE_TIME_END: " + req.SERVICE_TIME_END_time_stamp + "," 
					+ "READY_LIST_ENTRY: " + req.READY_LIST_ENTRY_time_stamp + "," 
					+ "TRANSMITTER_ENTRY: " + req.TRANSMITTER_ENTRY_time_stamp + "," 
					+ "TRANSMITTER_EXIT: " + req.TRANSMITTER_EXIT_time_stamp + "," 
					);
		}
	}
	
	private static LinkedList<IORequest> getIORequestsFromMeasurements(LinkedList<MeasurementEntry> measurements){
		LinkedList<IORequest> requests = new LinkedList<IORequest>();
		boolean isMeIdInRequests;
		IORequest request;
		
		//add measurements to appropriate request
		for(MeasurementEntry me:measurements) {
			isMeIdInRequests = false;
			for(int i=0; i<requests.size(); i++) {
				request = requests.get(i);
				if(request.id == me.id) {
					request.addMeasurement(me);
					isMeIdInRequests = true;
				}
			}
			if(!isMeIdInRequests) {
				//create new IORequest and put it in list of IORequests
				IORequest newReq = new IORequest();
				newReq.id = me.id;
				newReq.addMeasurement(me);
				requests.add(newReq);
			}
		}
		return requests;
	}
	
	*/
}

/*

class IORequest{
	public long id;
	public long STORAGE_API_ENTRY_time_stamp;
	public long ENTRY_LIST_ENTRY_time_stamp;
	public long ENTRY_LIST_EXIT_time_stamp;
	public long SERVICE_TIME_START_time_stamp;
	public long SERVICE_TIME_END_time_stamp;
	public long READY_LIST_ENTRY_time_stamp;
	public long TRANSMITTER_ENTRY_time_stamp;
	public long TRANSMITTER_EXIT_time_stamp;
	
	public void addMeasurement(MeasurementEntry me) {
		switch(me.timestampName) {
			case STORAGE_API_ENTRY:
				this.STORAGE_API_ENTRY_time_stamp = me.timestamp;
				break;
			case ENTRY_LIST_ENTRY:
				this.ENTRY_LIST_ENTRY_time_stamp = me.timestamp;
				break;
			case ENTRY_LIST_EXIT:
				this.ENTRY_LIST_EXIT_time_stamp = me.timestamp;
				break;
			case SERVICE_TIME_START:
				this.SERVICE_TIME_START_time_stamp = me.timestamp;
				break;
			case SERVICE_TIME_END:
				this.SERVICE_TIME_END_time_stamp = me.timestamp;
				break;
			case READY_LIST_ENTRY:
				this.READY_LIST_ENTRY_time_stamp = me.timestamp;
				break;
			case TRANSMITTER_ENTRY:
				this.TRANSMITTER_ENTRY_time_stamp = me.timestamp;
				break;
			case TRANSMITTER_EXIT:
				this.TRANSMITTER_EXIT_time_stamp = me.timestamp;
				break;
		}
	}
}

class TimeStampComparator implements Comparator<MeasurementEntry> {
	public int compare(MeasurementEntry e1, MeasurementEntry e2) {
		long longComparisonResult = e1.timestamp - e2.timestamp;
		int intComparisonResult;
		if(longComparisonResult > 0) {
			intComparisonResult = 1;
		}else if(longComparisonResult == 0) {
			intComparisonResult = 0;
		}else {
			intComparisonResult = -1;
		}
		return intComparisonResult;
	}
}


class RequestComparator implements Comparator<IORequest>{
	public int compare(IORequest r1, IORequest r2) {
		return Math.toIntExact(r1.STORAGE_API_ENTRY_time_stamp - r2.STORAGE_API_ENTRY_time_stamp);
	}
}



*/