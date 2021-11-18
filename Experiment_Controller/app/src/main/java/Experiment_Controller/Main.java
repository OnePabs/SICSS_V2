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
		Long[] runtimes = {(long) 10,(long)30};
		int runTimesConversionFactor = 1000;
		String resultsFileName = "/home/juancontreras/Downloads/results";
		boolean isVerbose = false;
		ParentScript script = new TechAConstant(
				interArrivalTime,
				serviceTime,
				runtimes,
				runTimesConversionFactor,
				storageApiAddress,
				applicationAddress,
				resultsFileName,
				isVerbose
				);
		script.run();
		
	}

}
