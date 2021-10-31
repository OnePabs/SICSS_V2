package scripts;

import common.ApplicationInterface;
import common.JsonToCsv;
import common.StorageAPIInterface;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


import java.util.LinkedList;
import java.util.Set;

/**
 * @author juancontreras
 *
 */
public class TechAConstant extends ParentScript{
	private Long interArrivalTime;
	private Long serviceTime;
	private Long[] runTimes;
	private int runTimeConversionFactorToMillis; //conversion factor to milliseconds
	private String storageAPIaddress;
	private String applicationAddress;
	private String resultsFileName;
	private boolean isVerbose;
	private int interOperationWaitTime = 1000;
	
	
	
	
	/**
	 * @param interArrivalTime: in milliseconds
	 * @param serviceTime: in milliseconds
	 * @param runTimes: experiment run times
	 * @param runTimeConversionFactorToMillis: conversion factor between runTimes units and milliseconds ex if runTimes is in seconds then runTimeConversionFactorToMillis=1000
	 */
	public TechAConstant(Long interArrivalTime, 
			Long serviceTime, 
			Long[] runTimes,
			int runTimeConversionFactorToMillis,
			String storageAPIaddress,
			String applicationAddress,
			String resultsFileName,
			boolean isVerbose
			) {
		this.interArrivalTime = interArrivalTime;
		this.serviceTime = serviceTime;
		this.runTimes = runTimes.clone();
		this.runTimeConversionFactorToMillis = runTimeConversionFactorToMillis;
		this.storageAPIaddress = storageAPIaddress;
		this.applicationAddress = applicationAddress;
		this.resultsFileName = resultsFileName;
		this.isVerbose = isVerbose;
	}
	
	
	@Override
	public void run() {
		System.out.println("Script:Technique A Constant script");
		System.out.println("Technique: A");
		System.out.println("Inter Arrival times distribution: Constant");
		System.out.println("Inter Arrival time: " + interArrivalTime);
		System.out.println("Service time: " + serviceTime);
		
		
		// Storage API Set up
		String jr = "{"
				+ "\"isVerbose\":true,"
				+ "\"dataTransferTechnique\":\"a\","
				+ "\"dataTransferTechniqueSettings\":{},"
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":"+ serviceTime +","
				+"\"useSleepForMockProcessing\":true,"
				+ "\"transmitter\":\"stub\""
				+ "}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
		
		try {
			Thread.sleep(interOperationWaitTime);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		//Application Set up
		String jr2 = "{"
				+ "\"receiverAddress\":\""+storageAPIaddress+"\","
				+ "\"isVerbose\":true,"
				+"\"useSleepForMockProcessing\":true,"
				+"\"interGenerationTimeDistribution\":\"CONSTANT\","
				+"\"interGenerationTimeDistributionSettings\":"+interArrivalTime
				+ "}";
		
		ApplicationInterface ai = new ApplicationInterface(applicationAddress);
		ai.changeSettings(jr2);
		try {
			Thread.sleep(interOperationWaitTime);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		for(int i=0; i<runTimes.length;i++) {
			System.out.println("Running experiment for: " + runTimes[i]);
			
			//start Storage API
			System.out.println("Starting storage API");
			sapi.start();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//Start Application
			System.out.println("Starting application");
			ai.start();
			
			//wait for experiment runtime
			try {
				Thread.sleep(runTimes[i]*runTimeConversionFactorToMillis);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//stop application
			System.out.println("Stopping application");
			ai.stop();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//stop Storage API
			System.out.println("Stopping storage api");
			sapi.stop();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			//Read measurements
			System.out.println("Reading Measurements");
			String measurements = sapi.getMeasurements();
			//System.out.println("JSON string received with measurements: ");
			
			//perform calculations and write measurements to file
			JsonToCsv.writeMeasurementsAndCalculations(measurements,resultsFileName,isVerbose);
			
			//clear measurements
			System.out.println("clearing storage API");
			sapi.clear();
			
			//trying to get measurements again
			System.out.println("trying to get measurements again");
			String measurements2 = sapi.getMeasurements();
			System.out.println("Measurements after clearing:");
			System.out.println(measurements2);
			
			
			
	        /*
			//writing measurements
	        System.out.println("writing Measurements");
		    try {
		        FileWriter myWriter = new FileWriter("TechAConst-ia"+interArrivalTime+"-st"+serviceTime+"-rt"+runTimes[i]+".txt");
		        if(jasonObject == null) {
		        	//there was a parsing error, write the string
		        	myWriter.write(measurements);
		        }else {
		        	//write measurements in csv format
		        	
		            Set<Object> keys = jsonObject.keySet();
		            for (Object key : keys) {
		            	String valueStr = jsonObject.get(key).toString();
		            	String keyStr = String.valueOf(key);
		            	String fileEntry = 
		            	myWriter.write(measurements);
		            }
		        }
		        
		        myWriter.close();
		        System.out.println("Successfully wrote to the file.");
		      } catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		      }
			System.out.println("Obtained measurements.");
			*/
			
		}
	}
	
}
