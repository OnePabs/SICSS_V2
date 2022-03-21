package Experiment_Controller;

import common.PerformanceMetrics;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");

		boolean isVerbose = false;
		
		//String application_location = "http://amazonaws:80";
		//String application_location = "http://localhost:8000";
		//String handler_location = "http://amazonaws:80";
		//String handler_location = "http://localhost:8080";
		//String manager_location = "http://amazonaws:80";
		//String manager_location = "http://localhost:8090";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {45*minutes_to_millis};

		//ARRIVAL TIMES
		int[] inter_arrival_times = {42};
		String inter_arrival_times_distribution;
		//inter_arrival_times_distribution = "CONSTANT";
		inter_arrival_times_distribution = "GEOMETRIC";


		//SERVICE TIMES
		int[] service_times = {40};
		String service_times_distribution;
		//service_times_distribution = "CONSTANT";
		service_times_distribution = "EXPONENTIAL";

		//RESULTS
		//String resultsFolderPath = "/home/ubuntu/results"; //linux
		//String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"; //laptop
		//String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results\\st-test"; //desktop

/*
		//Technique A
		ATechnique a0 = new ATechnique(
			isVerbose,
			runtimes,
			resultsFolderPath,
			application_location,
			inter_arrival_times,
			inter_arrival_times_distribution,
			handler_location,
			manager_location,
			service_times,
			service_times_distribution
		);
		a0.run();
*/
	
	
		//TECHNIQUE B
		int[] periods = {0,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,100,200,500};
		BTechnique b0 = new BTechnique(
			isVerbose,
			runtimes,
			resultsFolderPath,
			application_location,
			inter_arrival_times,
			inter_arrival_times_distribution,
			handler_location,
			periods,
			manager_location,
			service_times,
			service_times_distribution
		);
		b0.run();
		


/*
		//TECHNIQUE C
		int[] maxperiods = {300};
		int[] maxsizes = {1500};
		CTechnique c0 = new CTechnique(
			isVerbose,
			runtimes,
			resultsFolderPath,
			application_location,
			inter_arrival_times,
			inter_arrival_times_distribution,
			handler_location,
			maxperiods,
			maxsizes,
			manager_location,
			service_times,
			service_times_distribution
		);
		c0.run();
		*/
		
	}//end main
}//end class
