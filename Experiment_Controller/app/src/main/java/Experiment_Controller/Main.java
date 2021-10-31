package Experiment_Controller;

import common.ApplicationInterface;
import common.StorageAPIInterface;
import scripts.*;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String storageApiAddress = "http://localhost:8080";
		String applicationAddress = "http://localhost:8000";
		
		long interArrivalTime = (long)2000;
		long serviceTime = (long)1600;
		Long[] runtimes = {(long) 10};
		int runTimesConversionFactor = 1000;
		String resultsFileName = "/home/juancontreras/Downloads";
		boolean isVerbose = true;
		ParentScript script = new TechAConstant(interArrivalTime,
				serviceTime,
				runtimes,
				runTimesConversionFactor,
				storageApiAddress,
				applicationAddress,
				resultsFileName,
				isVerbose
				);
		script.run();
		
		/*
		
		// Storage API
		String jr = "{"
				+ "\"isVerbose\":true,"
				+ "\"dataTransferTechnique\":\"b\","
				+ "\"dataTransferTechniqueSettings\":{\"period\":3000},"
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":1600,"
				+"\"useSleepForMockProcessing\":true,"
				+ "\"transmitter\":\"stub\""
				+ "}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
		
		try {
			Thread.sleep(3000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		//start
		sapi.start();
		try {
			Thread.sleep(500);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		//send data
		sapi.sendData("hello world!");
		try {
			Thread.sleep(5000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		sapi.stop();
		try {
			Thread.sleep(5000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		//Application
		String jr2 = "{"
				+ "\"receiverAddress\":\"http://localhost:8080\","
				+ "\"isVerbose\":true,"
				+"\"useSleepForMockProcessing\":true,"
				+"\"interGenerationTimeDistribution\":\"CONSTANT\","
				+"\"interGenerationTimeDistributionSettings\":2000"
				+ "}";
		
		ApplicationInterface ai = new ApplicationInterface("http://localhost:8000");
		ai.changeSettings(jr2);
		try {
			Thread.sleep(500);
		}catch(Exception e) {
			
		}
		
		
		System.out.println("Starting application...");
		ai.start();
		
		try {
			Thread.sleep(10000);
		}catch(Exception e) {
			
		}
		
		System.out.println("Stopping application...");
		ai.stop();
		try {
			Thread.sleep(500);
		}catch(Exception e) {
			
		}
		
		
		
		System.out.println("Stopping storage api...");
		sapi.stop();
		try {
			Thread.sleep(500);
		}catch(Exception e) {
			
		}
		
		String measurements = sapi.getMeasurements();
		System.out.println("Obtained measurements: " + measurements);
		
		*/
		
	}

}
