package Experiment_Controller;

import common.PerformanceMetrics;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");

		boolean isVerbose = false;
		
		String application_location = "http://localhost:8000";
		String handler_location = "http://localhost:8080";
		String manager_location = "http://localhost:8090";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {45*minutes_to_millis};

		//ARRIVAL TIMES
		int[] inter_arrival_times = {100,80,50,45,43,41};
		String inter_arrival_times_distribution;
		//inter_arrival_times_distribution = "CONSTANT";
		inter_arrival_times_distribution = "GEOMETRIC";


		//SERVICE TIMES
		int[] service_times = {40};
		String service_times_distribution;
		//service_times_distribution = "CONSTANT";
		service_times_distribution = "EXPONENTIAL";

		//RESULTS
		String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results";




		//TECHNIQUE C
		int[] maxperiods = {60000};
		int[] maxsizes = {2000};
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

	}//end main
}//end class
