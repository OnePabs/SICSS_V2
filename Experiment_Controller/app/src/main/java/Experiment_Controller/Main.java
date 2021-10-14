package Experiment_Controller;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String jr = "{"
				+ "\"isVerbose\":true,"
				+ "\"dataTransferTechnique\":\"a\","
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":3000,"
				+"\"useSleepForMockProcessing\":true"
				+ "\"transmitter\":\"stub\""
				+ "}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
		
		sapi.changeSettings(jr);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		//start
		sapi.start();
		try {
			Thread.sleep(1000);
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
		
		String measurements = sapi.getMeasurements();
		System.out.println("Obtained measurements: " + measurements);
	}

}
