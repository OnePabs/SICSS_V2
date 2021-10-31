package common;

import java.util.*;

import common.TIMESTAMP_NAME;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JsonToCsv {
	
	public static void writeMeasurementsAndCalculations(String jsonArrayStr, String basePath, boolean isVerbose) {
		//parse and write measurements and calculations
		
		//parse json array string into a JSONArray
		JSONArray mesJsonArray = parseJsonArrayStr(jsonArrayStr);
		
		//map JSONArray into a linked list of MeasurementEntry objects
		LinkedList<MeasurementEntry> mes = mapJsonMeasurementsToMeasurementEntries(mesJsonArray);
		if(isVerbose) {
			printMeasurementEntries(mes);
		}
		
		//sort measurement entries by time stamp
		Collections.sort(mes, new TimeStampComparator());
		
		if(isVerbose) {
			System.out.println();
			System.out.println("---");
			System.out.println("Measurements sorted by time stamp");
			printMeasurementEntries(mes);
		}
		
		//write measurements to file
	}
	
	private static void writeMeasurementsToFile(String jsonArrayStr, String path) {
		//writes the measurement in csv file with headers requestID,STORAGE_API_ENTRY,...,TRANSMITTER_EXIT
		
	}
	
	
	
	private static void performAndWriteCalculationsToFile() {
		//writes the calculations from the measurements
		//headers are: 
		//Experiment Runtime, Arrival Rate, Residence Time, Throughput, MM1 Expected Residence Time, Average Number of jobs in the system, MM1 Expected Number of jobs in the system, Percentage Difference between observed and MM1 expected Calculations   
	}
	
	
	private static LinkedList<MeasurementEntry> sortMeasurementEntriesByTimeStamp(LinkedList<MeasurementEntry> mes){
		return null;
	}
	
	private static void printMeasurementEntries(LinkedList<MeasurementEntry> mes) {
		System.out.println("Number of measurement entries: " + mes.size());
		for(MeasurementEntry me: mes) {
			System.out.println("Id:"+me.id+" , TimeStamp Name: " + me.timestampName.toString() +" , TimeStamp value: " + me.timestamp);
		}
	}
	
	private static LinkedList<MeasurementEntry> mapJsonMeasurementsToMeasurementEntries(JSONArray jsonArray){
		LinkedList<MeasurementEntry> entries = new LinkedList<MeasurementEntry>();
		 Iterator i = jsonArray.iterator();
		 while(i.hasNext()) {
			 JSONObject jobj = (JSONObject)i.next();
			 MeasurementEntry me = new MeasurementEntry();
			 me.id = (long)jobj.get("requestId");
			 me.timestampName = TIMESTAMP_NAME.valueOf((String)jobj.get("TIMESTAMP_NAME"));
			 me.timestamp = (long)jobj.get("timeStamp");
			 entries.add(me);
		 }
		return entries;
	}
	
	
	private static JSONArray parseJsonArrayStr(String jsonArrayStr) {
		System.out.println("Parsing jsonArrayStr");
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
}

class MeasurementEntry {
	public long id;
	public TIMESTAMP_NAME timestampName;
	public long timestamp;
}

class TimeStampComparator implements Comparator<MeasurementEntry> {
	public int compare(MeasurementEntry e1, MeasurementEntry e2) {
		return Math.toIntExact(e1.timestamp - e2.timestamp);
	}
}


