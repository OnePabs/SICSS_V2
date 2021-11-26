package Experiment_Controller;

import common.ApplicationInterface;
import common.StorageAPIInterface;
import scripts.*;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String storageApiAddress = "http://localhost:8080";
		String applicationAddress = "http://localhost:8000";
		int runTimesConversionFactor = 1000;
		boolean isVerbose = true;
		
		/******* Technique A Constant  ********/
//		long interArrivalTimeAConstant = (long)2000;
//		long serviceTimeAConstant = (long)1600;
//		Long[] runtimesAConstant = {(long) 10,(long)30};
//		String resultsFileNameAConstant = "/home/juancontreras/Downloads/results";
//		ParentScript scriptAConstant = new TechAConstant(
//				interArrivalTimeAConstant,
//				serviceTimeAConstant,
//				runtimesAConstant,
//				runTimesConversionFactor,
//				storageApiAddress,
//				applicationAddress,
//				resultsFileNameAConstant,
//				isVerbose
//				);
//		scriptAConstant.run();
		
		
		
		/******* Technique B Constant  ********/
		long interArrivalTimeBConstant = (long)2000;
		long serviceTimeBConstant = (long)1600;
		long periodBConstant = interArrivalTimeBConstant;
		Long[] runtimesBConstant = {(long) 30/*,(long)30,(long)60*/};
		String resultsFileNameBConstant = "/home/juancontreras/Downloads/results";
		ParentScript scriptBConstant = new TechBConstant(
				interArrivalTimeBConstant,
				serviceTimeBConstant,
				periodBConstant,
				runtimesBConstant,
				runTimesConversionFactor,
				storageApiAddress,
				applicationAddress,
				resultsFileNameBConstant,
				isVerbose
				);
		scriptBConstant.run();

		
	}

}
