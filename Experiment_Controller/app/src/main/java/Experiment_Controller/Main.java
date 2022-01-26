package Experiment_Controller;

import common.ApplicationInterface;
import common.StorageAPIInterface;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String[] applicationAddresses = {"http://ec2-13-59-203-39.us-east-2.compute.amazonaws.com:80"};
		String[] storageApiAddress = {"http://ec2-3-137-173-19.us-east-2.compute.amazonaws.com:80", "http://ec2-18-119-165-22.us-east-2.compute.amazonaws.com:80"};
		String storageManagerAddress = "http://ec2-3-21-34-81.us-east-2.compute.amazonaws.com:80";
		boolean isVerbose = false;
		String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults";


		// Tech A constant Saturation experiment
		long runtime = 180000; //180 sec
		int[] interArrivalTimesInt = {280,250,220};
		String[] interArrivalTimesStr = new String[interArrivalTimesInt.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesInt.length;interArrivalIdx++){
			interArrivalTimesStr[interArrivalIdx] = String.valueOf(interArrivalTimesInt[interArrivalIdx]);
		}
		String aResultsFolderPath = resultsFolderPath + File.separator + "A";
		TechAConstantSaturation techaconstsaturation = new TechAConstantSaturation(
			isVerbose,
			runtime,
			interArrivalTimesStr,
			applicationAddresses,
			storageApiAddress,
			storageManagerAddress,
			aResultsFolderPath
		);
		techaconstsaturation.run();
		
		/*
		// Technique A Constant  
		long interArrivalTimeAConstant = (long)2000;
		long serviceTimeAConstant = (long)1600;
		Long[] runtimesAConstant = {(long) 10};
		String resultsFileNameAConstant = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\techadeb";
		ParentScript scriptAConstant = new TechAConstant(
				interArrivalTimeAConstant,
				serviceTimeAConstant,
				runtimesAConstant,
				runTimesConversionFactor,
				storageApiAddress,
				applicationAddress,
				storageManagerAddress,
				resultsFileNameAConstant,
				isVerbose
				);
		scriptAConstant.run();
		
		*/
		
		/******* Technique B Constant  ********/
//		long interArrivalTimeBConstant = (long)2000;
//		long serviceTimeBConstant = (long)1600;
//		long periodBConstant = interArrivalTimeBConstant;
//		Long[] runtimesBConstant = {(long) 30/*,(long)30,(long)60*/};
//		String resultsFileNameBConstant = "/home/juancontreras/Downloads/results";
//		ParentScript scriptBConstant = new TechBConstant(
//				interArrivalTimeBConstant,
//				serviceTimeBConstant,
//				periodBConstant,
//				runtimesBConstant,
//				runTimesConversionFactor,
//				storageApiAddress,
//				applicationAddress,
//				resultsFileNameBConstant,
//				isVerbose
//				);
//		scriptBConstant.run();
		
		
		/******* Technique C Constant  ********/
//		long interArrivalTimeCConstant = (long)2000;
//		long serviceTimeCConstant = (long)1600;
//		long periodCConstant = interArrivalTimeCConstant*2;
//		long maxsizeCConstant = 100;
//		Long[] runtimesCConstant = {(long) 30/*,(long)30,(long)60*/};
//		String resultsFileNameCConstant = "/home/juancontreras/Downloads/results";
//		ParentScript scriptCConstant = new TechCConstant(
//				interArrivalTimeCConstant,
//				serviceTimeCConstant,
//				periodCConstant,
//				maxsizeCConstant,
//				runtimesCConstant,
//				runTimesConversionFactor,
//				storageApiAddress,
//				applicationAddress,
//				resultsFileNameCConstant,
//				isVerbose
//				);
//		scriptCConstant.run();
		
		
		
		/******* Technique D Constant  ********/
//		long interArrivalTimeDConstant = (long)2000;
//		long serviceTimeDConstant = (long)1600;
//		Long[] runtimesDConstant = {(long) 30/*,(long)30,(long)60*/};
//		String resultsFileNameDConstant = "/home/juancontreras/Downloads/results";
//		ParentScript scriptDConstant = new TechDConstant(
//				interArrivalTimeDConstant,
//				serviceTimeDConstant,
//				runtimesDConstant,
//				runTimesConversionFactor,
//				storageApiAddress,
//				applicationAddress,
//				resultsFileNameDConstant,
//				isVerbose
//				);
//		scriptDConstant.run();
		
		
		
		/******* Technique E Constant  ********/
		/*
		long interArrivalTimeEConstant = (long)2000;
		long serviceTimeEConstant = (long)1600;
		Long[] runtimesEConstant = {(long) 30,(long)30,(long)60};
		String resultsFileNameEConstant = "/home/juancontreras/Downloads/results";
		ParentScript scriptEConstant = new TechEConstant(
				interArrivalTimeEConstant,
				serviceTimeEConstant,
				runtimesEConstant,
				runTimesConversionFactor,
				storageApiAddress,
				applicationAddress,
				resultsFileNameEConstant,
				isVerbose
				);
		scriptEConstant.run();
		*/


		/******* App and strg manager test constant  ********/
		/*
		storageManagerAddress += "/insertone";
		long interArrivalTimeConstant = (long)2000;
		Long[] runtimes = {(long) 30};
		ParentScript ApplicationStorageManagerTestScript = new ApplicationStorageManagerTest(
				interArrivalTimeConstant,
				runtimes,
				runTimesConversionFactor,
				applicationAddress,
				storageManagerAddress,
				isVerbose
		);
		ApplicationStorageManagerTestScript.run();
		 */

	}//end main
}//end class
