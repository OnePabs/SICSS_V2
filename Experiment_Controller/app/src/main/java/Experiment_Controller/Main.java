package Experiment_Controller;

import common.PerformanceMetrics;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");

		boolean isVerbose = false;
		
		//String application_location = "http://ec2-3-142-241-154.us-east-2.compute.amazonaws.com:80";
		String application_location = "http://ec2-3-15-168-141.us-east-2.compute.amazonaws.com:80";
		//String handler_location = "http://ec2-3-12-147-255.us-east-2.compute.amazonaws.com:80";
		String handler_location = "http://ec2-3-145-93-195.us-east-2.compute.amazonaws.com:80";
		//String manager_location = "http://ec2-3-16-155-240.us-east-2.compute.amazonaws.com:80";
		String manager_location = "http://ec2-18-219-245-236.us-east-2.compute.amazonaws.com:80";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {45*minutes_to_millis};

		//ARRIVAL TIMES
		int[] inter_arrival_times = {50};
		String inter_arrival_times_distribution;
		//inter_arrival_times_distribution = "CONSTANT";
		inter_arrival_times_distribution = "GEOMETRIC";


		//SERVICE TIMES
		int[] service_times = {40};
		String service_times_distribution;
		//service_times_distribution = "CONSTANT";
		service_times_distribution = "EXPONENTIAL";

		//RESULTS
		//String resultsFolderPath = "/home/ubuntu/results";
		String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results";

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
		int[] periods = {0,3,5,10,20,1000};
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
		int[] maxperiods = {73};
		int[] maxsizes = {300};
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
