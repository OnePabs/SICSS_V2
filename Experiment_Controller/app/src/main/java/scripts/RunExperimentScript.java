package scripts;

import common.*;
import java.util.LinkedList;
import java.io.File;

/**
 * @author juancontreras
 *
 */
public class RunExperimentScript {
	private static long interOperationWaitTime = 1000; //1 second

	public static void runExperiment(LinkedList<ExperimentParameter> experimentParameters, String result_folder_path) throws Exception{
		for(ExperimentParameter exp:experimentParameters){
			try{
				runExperiment(exp,result_folder_path);
			}catch(Exception e){
				System.out.println("Error while running or analysis of experiment: " + exp.experimentName);
				e.printStackTrace();
			}
		}
	}

	public static void runExperiment(ExperimentParameter experimentParameters, String result_folder_path) throws Exception{
		/*
		//print parameters
		System.out.println("results folder path="+result_folder_path);
		System.out.println();
		for(ExperimentParameter expara:experimentParameters){
			expara.print();
			System.out.println();
			System.out.println();
		}
		System.out.println();
		System.out.println();
		 */

		//stop applications
		for(ApplicationInterface api:experimentParameters.applications_interfaces){
			api.stop();
		}
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//change application settings
		for(ApplicationInterface api:experimentParameters.applications_interfaces){
			api.changeSettings(experimentParameters.applications_parameters);
		}
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//stop storage API
		experimentParameters.storageApis_interface.stop();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//clear storage api
		experimentParameters.storageApis_interface.clear();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//change storage api settings
		experimentParameters.storageApis_interface.changeSettings(experimentParameters.storageApi_parameters);
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//clear storage manager
		experimentParameters.storageManager_interface.clear();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//change storage manager settings
		experimentParameters.storageManager_interface.changeSettings(experimentParameters.storageManager_parameters);
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//start storage api
		experimentParameters.storageApis_interface.start();
		//inter operation time
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}

		//start applications
		for(ApplicationInterface api:experimentParameters.applications_interfaces){
			api.start();
		}

		//// RUN EXPERIMENT TIME ////
		//sleep for experiment runtime
		try{
			Thread.sleep(experimentParameters.experimentRuntime);
		}catch(Exception e){
			e.printStackTrace();
		}

		//stop applications
		for(ApplicationInterface api:experimentParameters.applications_interfaces){
			api.stop();
		}
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//Wait for All requests to be serviced
		// try{
		// 	Thread.sleep(experimentParameters.experimentRuntime); //wait for experiment run time to allow all requests to be transmitted from storage API to storage Manager and stats to be taken
		// }catch(Exception e){
		// 	e.printStackTrace();
		// }

		//stop storage API
		experimentParameters.storageApis_interface.stop();
		//inter operation time
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}

		//get storage api measurements
		String storageApi_measurements = experimentParameters.storageApis_interface.getMeasurements();

		//store storage api measurements
		String experiment_results_folder = result_folder_path + File.separator + experimentParameters.experimentName;
		LinkedList<MeasurementEntry> strgApi_EntryMeasurements = JsonToCsv.getAndStoreMeasurements(storageApi_measurements,experiment_results_folder,"strgapi");

		//get storage manager measurements
		String storageManager_measurements = experimentParameters.storageManager_interface.getMeasurements();

		//store storage manager measurements
		LinkedList<MeasurementEntry> strgMngr_EntryMeasurements = JsonToCsv.getAndStoreMeasurements(storageManager_measurements,experiment_results_folder,"strgMngr");

		
		System.out.println("Experiment " + experimentParameters.experimentName + "finished running and results are stored");
		
		/*
		//get and store storage api performance metrics
		MeasurementsComparator comp = new MeasurementsComparator();
		strgApi_EntryMeasurements.sort(comp);
		JsonToCsv.writeStorageApiPerformanceMetrics(strgApi_EntryMeasurements,experiment_results_folder);

		//get and store storage manager performance metrics
		strgMngr_EntryMeasurements.sort(comp);
		JsonToCsv.writeStorageManagerPerformanceMetrics(strgMngr_EntryMeasurements,experiment_results_folder);

		System.out.println("Experiment " + experimentParameters.experimentName + "finished Analysis");
		*/

		//clear storage api
		experimentParameters.storageApis_interface.clear();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//clear storage manager
		experimentParameters.storageManager_interface.clear();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	public static void runExperiment(
		String[] application_locations,
		boolean is_num_app_runs_binomial,	//if true run: app 1, then app1 AND app2, then App1 AND app2 AND app3,..., if false, run app1, then run only app2, then only app3...
		String[] app_json_parameter_configurations,
		String[] storageApi_locations,
		boolean is_num_storageApi_runs_binomial,
		String[] storageApi_json_parameter_configurations,
		String storageManager_location,
		long[] runTimes,
		String result_folder_path
	){
		int application_location_idx;
		int app_json_parameter_configurations_idx;
		int storageApi_locations_idx;
		int storageApi_json_parameter_configurations_idx;
		int runTimes_idx;

		if(is_num_app_runs_binomial && !is_num_storageApi_runs_binomial){
			for(application_location_idx = 0; application_location_idx < application_locations.length; application_location_idx++){
				System.out.print("Applications: ");

				//create application interfaces
				String apps = "";
				for(int i=0; i<=application_location_idx; i++){
					apps += application_locations[i];
					apps += ",";
				}
				System.out.println();

				//Application parameters
				for(app_json_parameter_configurations_idx=0; app_json_parameter_configurations_idx<app_json_parameter_configurations.length; app_json_parameter_configurations_idx++){
					//storage api locations
					for(storageApi_locations_idx=0;storageApi_locations_idx<storageApi_locations.length;storageApi_locations_idx++){
						//storage api parameters
						for(storageApi_json_parameter_configurations_idx=0;storageApi_json_parameter_configurations_idx<storageApi_json_parameter_configurations.length;storageApi_json_parameter_configurations_idx++){
							
							//Run experiment(s)
							for(runTimes_idx=0;runTimes_idx<runTimes.length;runTimes_idx++){
								System.out.println("runtime: " + String.valueOf(runTimes[runTimes_idx]));
								System.out.println("applications: " + apps);
								System.out.println("app_config: " + app_json_parameter_configurations[app_json_parameter_configurations_idx]);
								System.out.println("storageApi location: " + storageApi_locations[storageApi_locations_idx]);
								System.out.println("storageApi config: " + storageApi_json_parameter_configurations[storageApi_json_parameter_configurations_idx]);
								
								String folder_name = "exp_";
								folder_name += String.valueOf(application_location_idx) + "_";
								folder_name += String.valueOf(app_json_parameter_configurations_idx) + "_";
								folder_name += String.valueOf(storageApi_locations_idx) + "_";
								folder_name += String.valueOf(storageApi_json_parameter_configurations_idx) + "_";
								folder_name += String.valueOf(runTimes_idx) + "_exp";

								System.out.println("folder_name: " + folder_name);
								System.out.println();
								System.out.println();
							}
						}
					}
				}


				System.out.println();
			}
		}else{
			System.out.println("Wrong binomial parameters... not implemented yet");
		}
	}
	*/

	
	/*
	public static void RunSaturationExperiment (
			double[] applicationsTrhoughput,
			double storageApiProgrammedDelay,
			long runtime,
			int runTimeConversionFactorToMillis,
			LinkedList<ApplicationInterface> ais,
			LinkedList<StorageAPIInterface> sapis,
			StorageManagerInterface smi,
			String baseResultspath,
			boolean isVerbose
		) throws Exception{
		for(int i=0; i<runTimes.length;i++) {

			System.out.println("Running experiment for: " + runTimes[i]); //always display the experiment runtime. for user purposes
			
			//clear storage manager measurements
			if(isVerbose) {
				System.out.println("clearing storage manager");
			}
			smi.clear();

			//clear storage API measurements
			if(isVerbose) {
				System.out.println("clearing storage apis");
			}
			for(StorageAPIInterface sapi: sapis){
				sapi.clear();
			}

			//start Storage APIs
			if(isVerbose) {
				System.out.println("Starting storage APIs");
			}
			for(StorageAPIInterface sapi: sapis){
				sapi.start();
			}

			//wait for inter operation time
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//Start Applications
			if(isVerbose) {
				System.out.println("Starting applications");
			}
			for(ApplicationInterface ai: ais){
				ai.start();
			}

			//wait for experiment runtime
			try {
				Thread.sleep(runTimes[i]*runTimeConversionFactorToMillis);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//stop applications
			if(isVerbose) {
				System.out.println("Stopping applicationa");
			}
			for(ApplicationInterface ai: ais){
				ai.stop();
			}

			//wait for inter operation time
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//stop Storage API
			if(isVerbose) {
				System.out.println("Stopping storage api");
			}
			for(StorageAPIInterface sapi: sapis){
				sapi.stop();
			}

			//wait for inter operation time
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			//Read measurements
			if(isVerbose) {
				System.out.println("Reading Measurements");
			}

			//read and store storage API measurements and performance metrics
			for(int i=0; i<sapis.size(); i++){
				String moduleName = String.valueOf(i) + "sapi";
				JsonToCsv.getAndStoreMeasurements(sapis.get(i).getMeasurements(),baseResultspath,moduleName);
			}

			//read and store storage manager measurements and performance metrics
			String storageManagerMeasurements = smi.getMeasurements();
			JsonToCsv.getAndStoreMeasurements(sapis.get(i).getMeasurements(),baseResultspath,"strgManager");

			
			//Storage API last state is STOPPED
		}
	}


	/**
	 * @param ai: application interface
	 * @param sapi: storage api interface
	 * @param runTimes: array of experiment runtimes. Units have to be the same for all runTimes
	 * @param runTimeConversionFactorToMillis: conversion factor between runTimes units and milliseconds ex if runTimes is in seconds then runTimeConversionFactorToMillis=1000
	 * @param resultspath: path where the results of the experiment are to be written to (base path)
	 * @param isVerbose
	 * Storage API state after running the experiments is STOPPED
	 */
	/*
	public static void RunLongRunExperiment (
			ApplicationInterface ai,
			StorageAPIInterface sapi,
			double storageApiServiceRate,
			Long[] runTimes,
			int runTimeConversionFactorToMillis,
			String baseResultspath,
			boolean includeExpectedMM1Values,
			boolean isVerbose
	) throws Exception{
		for(int i=0; i<runTimes.length;i++) {

			System.out.println("Running experiment for: " + runTimes[i]); //always display the experiment runtime. for user purposes


			//start Storage API
			if(isVerbose) {
				System.out.println("Starting storage API");
			}
			sapi.start();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}

			//Start Application
			if(isVerbose) {
				System.out.println("Starting application");
			}
			ai.start();

			//wait for experiment runtime
			try {
				Thread.sleep(runTimes[i]*runTimeConversionFactorToMillis);
			}catch(Exception e) {
				e.printStackTrace();
			}

			//stop application
			if(isVerbose) {
				System.out.println("Stopping application");
			}
			ai.stop();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}

			//stop Storage API
			if(isVerbose) {
				System.out.println("Stopping storage api");
			}
			sapi.stop();
			try {
				Thread.sleep(interOperationWaitTime);
			}catch(Exception e) {
				e.printStackTrace();
			}


			//Read measurements
			if(isVerbose) {
				System.out.println("Reading Measurements");
			}
			String measurements = sapi.getMeasurements();

			//creating new Json to csv object
			JsonToCsv jcsv = new JsonToCsv(measurements,isVerbose);

			//writing measurements to file
			jcsv.writeMeasurementsToFile(baseResultspath + String.valueOf(runTimes[i]) + "-");

			//write basic calculations to file
			jcsv.writeBasicCalculationsToFile(storageApiServiceRate,baseResultspath,includeExpectedMM1Values);

			//clear storage api
			if(isVerbose) {
				System.out.println("clearing storage API");
			}
			sapi.clear();

			//resulting last state is STOPPED
		}
	}

	 */
}
