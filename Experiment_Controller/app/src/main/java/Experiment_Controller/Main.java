package Experiment_Controller;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		// Storage API
		String jr = "{"
				+ "\"isVerbose\":true,"
				+ "\"dataTransferTechnique\":\"a\","
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":40,"
				+"\"useSleepForMockProcessing\":true,"
				+ "\"transmitter\":\"stub\""
				+ "}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
		
		try {
			Thread.sleep(500);
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
		
		/*
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
		*/
		
		
		//Application
		String jr2 = "{"
				+ "\"receiverAddress\":\"http://localhost:8080\","
				+ "\"isVerbose\":true,"
				+"\"useSleepForMockProcessing\":true,"
				+"\"interGenerationTimeDistribution\":\"CONSTANT\","
				+"\"interGenerationTimeDistributionSettings\":50"
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
			Thread.sleep(3000);
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
		
	}

}
