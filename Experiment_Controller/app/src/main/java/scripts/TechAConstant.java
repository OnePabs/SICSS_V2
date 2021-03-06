package scripts;

import common.ApplicationInterface;
import common.JsonToCsv;
import common.StorageAPIInterface;

import java.io.File;
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
	private String storageManagerAddress;
	private String resultsFolderPath;
	private boolean isVerbose;
	private int interOperationWaitTime = 3000;
	
	
	
	
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
			String storageManagerAddress,
			String resultsFolderPath,
			boolean isVerbose
			) {
		this.interArrivalTime = interArrivalTime;
		this.serviceTime = serviceTime;
		this.runTimes = runTimes.clone();
		this.runTimeConversionFactorToMillis = runTimeConversionFactorToMillis;
		this.storageAPIaddress = storageAPIaddress;
		this.applicationAddress = applicationAddress;
		this.storageManagerAddress = storageManagerAddress;
		this.resultsFolderPath = resultsFolderPath;
		this.isVerbose = isVerbose;
	}
	
	
	@Override
	public void run() {
		/*
		System.out.println("Script:Technique A Constant script");
		System.out.println("Technique: A");
		System.out.println("Inter Arrival times distribution: Constant");
		System.out.println("Inter Arrival time: " + interArrivalTime);
		System.out.println("Service time: " + serviceTime);
		
		String[] app_locations = {"app_location_1", "app_location_2"};
		boolean is_num_app_runs_binomial = true;
		String[] app_json_parameter_configurations = {"app_config_1", "app_config_2", "app_config_3"};
		String[] storageApi_locations = {"storageApi_location_1", "storageApi_location_2"};
		boolean is_num_storageApi_runs_binomial = false;
		String[] storageApi_json_parameter_configurations = {"storageApi_config_1", "storageApi_config_2", "storageApi_config_3"};
		String storageManager_location = "storageManager_location";
		String result_folder_path = "home";
		long[] runTimes = {(long)10, (long)20};

		RunExperimentScript.runExperiment(
			app_locations,
			is_num_app_runs_binomial,
			app_json_parameter_configurations,
			storageApi_locations,
			is_num_storageApi_runs_binomial,
			storageApi_json_parameter_configurations,
			storageManager_location,
			runTimes,
			result_folder_path
		);
		*/

		/*
		//Storage Manager set up
		StorageManagerInterface smi = new StorageManagerInterface(storageManagerAddress);
		smi.clear();
		
		// Storage API Set up
		String jr = "{"
				+ "\"isVerbose\":" + String.valueOf(isVerbose) + ","
				+ "\"dataTransferTechnique\":\"a\","
				+ "\"dataTransferTechniqueSettings\":{},"
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":"+ serviceTime +","
				+"\"useSleepForMockProcessing\":true,"
				+ "\"transmitter\":\"StorageManagerTransmitter\","
				+ "\"destination\":\"" + storageManagerAddress + "\","
				+ "}";
		
		StorageAPIInterface sapi = new StorageAPIInterface(storageAPIaddress);
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
				+ "\"receiverAddress\":\""+storageAPIaddress+"/data"+"\","
				+ "\"isVerbose\":" + String.valueOf(isVerbose) + ","
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
		
		double serviceRate = 1/(double)serviceTime;
		
		//create results directory
		String resultsDirectory = resultsFolderPath + "/Tech-A-const-iat-"+String.valueOf(interArrivalTime) +"-st-" + String.valueOf(serviceTime);
		new File(resultsDirectory).mkdirs();
		
		String resultsBasePath = resultsDirectory + "/";
		try {
			RunExperimentScript.RunExperiment(ai, sapi,serviceRate , runTimes, runTimeConversionFactorToMillis, resultsBasePath, false, isVerbose);
		}catch(Exception e) {
			if(isVerbose) {
				System.out.println("Technique A constant: could not complete experiment");
			}
			e.printStackTrace();
		}
		*/
	}
	
}
