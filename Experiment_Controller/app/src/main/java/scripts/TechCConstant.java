package scripts;

import java.io.File;

import common.ApplicationInterface;
import common.StorageAPIInterface;

public class TechCConstant extends ParentScript{
	private Long interArrivalTime; // in milliseconds
	private Long serviceTime; // in milliseconds
	private Long period; // in milliseconds
	private Long maxsize;
	private Long[] runTimes;
	private int runTimeConversionFactorToMillis; //conversion factor to milliseconds
	private String storageAPIaddress;
	private String applicationAddress;
	private String resultsFolderPath;
	private boolean isVerbose;
	private int interOperationWaitTime = 1000;
	
	
	
	public TechCConstant(Long interArrivalTime, Long serviceTime, Long period, Long maxsize, Long[] runTimes,
			int runTimeConversionFactorToMillis, String storageAPIaddress, String applicationAddress,
			String resultsFolderPath, boolean isVerbose) {
		super();
		this.interArrivalTime = interArrivalTime;
		this.serviceTime = serviceTime;
		this.period = period;
		this.maxsize = maxsize;
		this.runTimes = runTimes;
		this.runTimeConversionFactorToMillis = runTimeConversionFactorToMillis;
		this.storageAPIaddress = storageAPIaddress;
		this.applicationAddress = applicationAddress;
		this.resultsFolderPath = resultsFolderPath;
		this.isVerbose = isVerbose;
	}



	@Override
	public void run() {
		System.out.println("Script:Technique C Constant");
		System.out.println("Inter Arrival time: " + interArrivalTime);
		System.out.println("Service time: " + serviceTime);
		System.out.println("Period: " + period);
		System.out.println("maxsize: " + maxsize);
		
		
		// Storage API Set up
		String jr = "{"
				+ "\"isVerbose\":" + String.valueOf(isVerbose) + ","
				+ "\"dataTransferTechnique\":\"c\","
				+ "\"dataTransferTechniqueSettings\":{\"period\":" + period +",\"maxsize\":"+ maxsize +"},"
				+ "\"serviceTimeDistribution\":\"CONSTANT\","
				+ "\"serviceTimeDistributionSettings\":"+ serviceTime +","
				+"\"useSleepForMockProcessing\":true,"
				+ "\"transmitter\":\"stub\""
				+ "}";
		StorageAPIInterface sapi = new StorageAPIInterface(storageAPIaddress);
		if(!sapi.changeSettings(jr)) {
			if(isVerbose) {
				System.out.println("Could not change the settings of Storage API");
			}
			return;
		}else if(isVerbose) {
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
		String resultsDirectory = resultsFolderPath 
				+ "/Tech-C-const-iat-"+String.valueOf(interArrivalTime) 
				+ "-st-" + String.valueOf(serviceTime) 
				+ "-p-"+String.valueOf(period)
				+ "-sz-" + String.valueOf(maxsize);
		new File(resultsDirectory).mkdirs();
		
		String resultsBasePath = resultsDirectory + "/";
		try {
			RunExperimentScript.RunExperiment(ai, sapi,serviceRate , runTimes, runTimeConversionFactorToMillis, resultsBasePath, false, isVerbose);
		}catch(Exception e) {
			if(isVerbose) {
				System.out.println("Technique C constant: could not complete experiment");
			}
			e.printStackTrace();
		}
	}
}
