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
		final int MAX_NUM_TRIES = 3;
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

		//stop application
		experimentParameters.applications_interface.stop();
		//inter operation time
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}

		//change application settings
		experimentParameters.applications_interface.changeSettings(experimentParameters.applications_parameters);
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

		System.out.println("Starting experiment: " + experimentParameters.experimentName);

		//start storage api
		experimentParameters.storageApis_interface.start();
		//inter operation time
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}

		//start application
		experimentParameters.applications_interface.start();

		//// RUN EXPERIMENT TIME ////
		//sleep for experiment runtime
		try{
			Thread.sleep(experimentParameters.experimentRuntime);
		}catch(Exception e){
			e.printStackTrace();
		}

		//stop application
		experimentParameters.applications_interface.stop();
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
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}

		//Get and store Storage Handler Measurements
		String experiment_results_folder = result_folder_path + File.separator + experimentParameters.experimentName;
		int i;
		for(i=0;i<MAX_NUM_TRIES;i++){
			try{
				//get storage handler measurements
				String storageApi_measurements = experimentParameters.storageApis_interface.getMeasurements();

				//store storage api measurements
				LinkedList<MeasurementEntry> strgApi_EntryMeasurements = JsonToCsv.getAndStoreMeasurements(storageApi_measurements,experiment_results_folder,"strgapi");

				break;
			}catch(Exception e){
				System.out.println("Error Getting Storage Handler measurements. Trying again...");
				try{
					Thread.sleep(1000);
				}catch(Exception ex){}
			}
		}
		if(i==MAX_NUM_TRIES){
			System.out.println("Final - could not get Storage Handler measurements");
			return;
		}
		

		//get and store storage manager measurements
		for(i=0;i<MAX_NUM_TRIES;i++){
			try{
				//get manager measurements
				String storageManager_measurements = experimentParameters.storageManager_interface.getMeasurements();

				//store storage manager measurements
				LinkedList<MeasurementEntry> strgMngr_EntryMeasurements = JsonToCsv.getAndStoreMeasurements(storageManager_measurements,experiment_results_folder,"strgMngr");
				break;
			}catch(Exception e){
				System.out.println("Error Getting Storage Manager measurements. Trying again...");
				try{
					Thread.sleep(1000);
				}catch(Exception ex){}
			}
		}
		if(i == MAX_NUM_TRIES){
			System.out.println("Final - could not get Storage Manager measurements");
			return;
		}
		

		
		
		System.out.println("Experiment " + experimentParameters.experimentName + "finished running and results are stored");

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
}
