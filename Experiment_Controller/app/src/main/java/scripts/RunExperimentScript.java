package scripts;

import common.*;

/**
 * @author juancontreras
 *
 */
public class RunExperimentScript {
	private static long interOperationWaitTime = 1000; //1 second
	

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
