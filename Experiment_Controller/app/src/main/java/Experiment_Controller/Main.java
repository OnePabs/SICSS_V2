package Experiment_Controller;

import common.PerformanceMetrics;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String[] applicationAddresses = {"http://localhost:8000"};
		String[] storageApiAddress = {"http://localhost:8080"};
		String storageManagerAddress = "http://localhost:8090";
		boolean isVerbose = false;
		long minutes_to_millis = (long)60000;
		long[] runtime = {45*minutes_to_millis};
		int[] ias;
		long[] serviceTime = {40};
		long maxperiod = 60000; //for technique C
		String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results";


		/*
		// Tech A constant Saturation experiment
		int[] interArrivalTimesInt = {40};
		String[] interArrivalTimesStr = new String[interArrivalTimesInt.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesInt.length;interArrivalIdx++){
			interArrivalTimesStr[interArrivalIdx] = String.valueOf(interArrivalTimesInt[interArrivalIdx]);
		}
		String aResultsFolderPath = resultsFolderPath + File.separator + "A";
		TechAConstantStubSaturation techaconststubsaturation = new TechAConstantStubSaturation(
			isVerbose,
			runtime,
			serviceTime,
			interArrivalTimesStr,
			applicationAddresses,
			storageApiAddress,
			storageManagerAddress,
			aResultsFolderPath
		);
		techaconststubsaturation.run();
		 */

		/*
		//Experiment one - Tech B periods
		//Technique B - vary periods
		int[] interArrivalTimesPeriods = {10000};//{200,100,80,50,45,40};
		long[] periods = {1000};
		String[] interArrivalTimesBperiodsStr = new String[interArrivalTimesPeriods.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesPeriods.length;interArrivalIdx++){
			interArrivalTimesBperiodsStr[interArrivalIdx] = String.valueOf(interArrivalTimesPeriods[interArrivalIdx]);
		}
		String bPeriodsResultsFolderPath = resultsFolderPath + File.separator + "B-exp-periods-test";
		TechBExponentialStubSaturation techBExponentialStubPeriods = new TechBExponentialStubSaturation(
				isVerbose,
				runtime,
				serviceTime,
				periods,
				interArrivalTimesBperiodsStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				bPeriodsResultsFolderPath
		);
		techBExponentialStubPeriods.run();
*/


		///// TECH B EXPONENTIAL  ///////
		System.out.println("Running Technique B - Periods");
		ias  = new int[]{50};
		long[] periodBexp = {500,200,100,50,20,10,5};
		String[] iasStr = new String[ias.length];
		for(int interArrivalIdx=0;interArrivalIdx<ias.length;interArrivalIdx++){
			iasStr[interArrivalIdx] = String.valueOf(ias[interArrivalIdx]);
		}
		String bSaturationResultsFolderPath = resultsFolderPath + File.separator + "B-exp-periods";
		TechBExponentialStubStorage techBExponentialStubStorage = new TechBExponentialStubStorage(
				isVerbose,
				runtime,
				serviceTime,
				periodBexp,
				iasStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				bSaturationResultsFolderPath
		);
		techBExponentialStubStorage.run();

		System.out.println("Running Technique B - Saturation");
		ias = new int[]{41, 50, 80};
		periodBexp = new long[]{1000};
		iasStr = new String[ias.length];
		for(int interArrivalIdx=0;interArrivalIdx<ias.length;interArrivalIdx++){
			iasStr[interArrivalIdx] = String.valueOf(ias[interArrivalIdx]);
		}
		bSaturationResultsFolderPath = resultsFolderPath + File.separator + "B-exp-saturation";
		techBExponentialStubStorage = new TechBExponentialStubStorage(
				isVerbose,
				runtime,
				serviceTime,
				periodBexp,
				iasStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				bSaturationResultsFolderPath
		);
		techBExponentialStubStorage.run();


/*
		// Experiment 2 - A saturation at 24 requests per second
		/////////  TECH A EXPONENTIAL ////////////
		int[] interArrivalTimesA = {10000};
		String[] interArrivalTimesAStr = new String[interArrivalTimesA.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesA.length;interArrivalIdx++){
			interArrivalTimesAStr[interArrivalIdx] = String.valueOf(interArrivalTimesA[interArrivalIdx]);
		}
		String aResultsFolderPath = resultsFolderPath + File.separator + "A-exp-saturation";
		TechAExponentialStubSaturation techAExponentialStubSaturation = new TechAExponentialStubSaturation(
				isVerbose,
				runtime,
				serviceTime,
				interArrivalTimesAStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				aResultsFolderPath
		);
		techAExponentialStubSaturation.run();
*/

/*
		///// TECH B EXPONENTIAL  ///////
		int[] interArrivalTimesBsaturation = {10000};
		long[] periodBexp = {5000};
		String[] interArrivalTimesBsaturationStr = new String[interArrivalTimesBsaturation.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesBsaturation.length;interArrivalIdx++){
			interArrivalTimesBsaturationStr[interArrivalIdx] = String.valueOf(interArrivalTimesBsaturation[interArrivalIdx]);
		}
		String bSaturationResultsFolderPath = resultsFolderPath + File.separator + "B-exp-saturation";
		TechBExponentialStubStorage techBExponentialStubStorage = new TechBExponentialStubStorage(
				isVerbose,
				runtime,
				serviceTime,
				periodBexp,
				interArrivalTimesBsaturationStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				bSaturationResultsFolderPath
		);
		techBExponentialStubStorage.run();
*/

/*
		//Tech C exponential
		int[] interArrivalTimesCSaturation = {80,50,41};
		long[] maxsizesCsaturation = {2000}; //when request rate is 20, data transfers will occur one per second
		String[] interArrivalTimesCsaturationStr = new String[interArrivalTimesCSaturation.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesCSaturation.length;interArrivalIdx++){
			interArrivalTimesCsaturationStr[interArrivalIdx] = String.valueOf(interArrivalTimesCSaturation[interArrivalIdx]);
		}
		String cSaturationResultsFolderPath = resultsFolderPath + File.separator + "C-exp-saturation";
		TechCExponentialStubSaturation techCExponentialStubSaturation = new TechCExponentialStubSaturation(
				isVerbose,
				runtime,
				serviceTime,
				maxperiod,
				maxsizesCsaturation,
				interArrivalTimesCsaturationStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				cSaturationResultsFolderPath
		);
		techCExponentialStubSaturation.run();
*/


/*
		//Experiment 3 - C exp vary max sizes
		int[] interArrivalTimesCmaxSizes = {50};
		long[] maxsizesCmaxSizes = {50,30};
		String[] interArrivalTimesCmaxsizesStr = new String[interArrivalTimesCmaxSizes.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesCmaxSizes.length;interArrivalIdx++){
			interArrivalTimesCmaxsizesStr[interArrivalIdx] = String.valueOf(interArrivalTimesCmaxSizes[interArrivalIdx]);
		}
		String cMaxsizesResultsFolderPath = resultsFolderPath + File.separator + "C-exp-maxsizes-feb07";
		TechCExponentialStubSaturation techCExponentialStubMaxsizes = new TechCExponentialStubSaturation(
				isVerbose,
				runtime,
				serviceTime,
				maxperiod,
				maxsizesCmaxSizes,
				interArrivalTimesCmaxsizesStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				cMaxsizesResultsFolderPath
		);
		techCExponentialStubMaxsizes.run();
*/


		/*
		String[] measurementFolders = {
				"C:\\Users\\juanp\\Documents\\experiment_results\\A_test3\\A-0-0-50\\measurements"
		};
		String resultsFolder = "C:\\Users\\juanp\\Documents\\experiment_results\\performance";

		for(int i=0; i<measurementFolders.length;i++){
			System.out.println("working on: " + measurementFolders[i]);
			String apiMeasurementsFile = measurementFolders[i] + "\\"+ "strgapi.txt";
			String apiResultsFile = resultsFolder + "\\" + "api-"+String.valueOf(i) + ".txt";
			PerformanceMetrics.writeStorageApiPerformanceMetrics(apiMeasurementsFile,apiResultsFile);

			String mngrMeasurementsFile =  measurementFolders[i] + "\\"+ "strgMngr.txt";
			String mngrResultsFile = resultsFolder + "\\" + "mngr-"+String.valueOf(i) + ".txt";
			PerformanceMetrics.writeStorageManagerPerformanceMetrics(mngrMeasurementsFile,mngrResultsFile);
		}
*/







/*
		//technique C exponential stub
		int[] interArrivalTimesInt = {50}; //arrival rates: 1, 2, 5, 10, 12.5, 20
		long serviceTime = 40;
		long maxperiod = 60000;
		long[] maxsizes = {2000}; //when request rate is 20, data transfers will occur one per second
		String[] interArrivalTimesStr = new String[interArrivalTimesInt.length];
		for(int interArrivalIdx=0;interArrivalIdx<interArrivalTimesInt.length;interArrivalIdx++){
			interArrivalTimesStr[interArrivalIdx] = String.valueOf(interArrivalTimesInt[interArrivalIdx]);
		}
		String aResultsFolderPath = resultsFolderPath + File.separator + "C-exp";
		TechCExponentialStubSaturation techCExponentialStubSaturationstubsaturation = new TechCExponentialStubSaturation(
				isVerbose,
				runtime,
				serviceTime,
				maxperiod,
				maxsizes,
				interArrivalTimesStr,
				applicationAddresses,
				storageApiAddress,
				storageManagerAddress,
				aResultsFolderPath
		);
		techCExponentialStubSaturationstubsaturation.run();






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
