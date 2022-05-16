package Experiment_Controller;

import common.*;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");

		boolean isVerbose = true;
		
		//String application_location = "http://ec2-3-133-153-208.us-east-2.compute.amazonaws.com:80";
		String application_location = "http://localhost:8000";
		//String handler_location = "http://ec2-3-139-81-140.us-east-2.compute.amazonaws.com:80";
		String handler_location = "http://localhost:8080";
		//String manager_location = "http://35.203.51.116:80";
		String manager_location = "http://localhost:8090";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {10000}; //{20*minutes_to_millis};

		//ARRIVAL TIMES
		int[] inter_arrival_times = {10000};
		String inter_arrival_times_distribution;
		inter_arrival_times_distribution = "CONSTANT";
		//inter_arrival_times_distribution = "GEOMETRIC";


		//SERVICE TIMES
		int[] service_times = {5000};
		String service_times_distribution;
		service_times_distribution = "CONSTANT";
		//service_times_distribution = "EXPONENTIAL";
		boolean useStep=false;

		//RESULTS
		//String resultsFolderPath = "/home/ubuntu/results"; //linux
		//String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"; //laptop
		//String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results\\measurements"; //desktop
		String resultsFolderPath = "C:\\Users\\juanp\\OneDrive\\Documents\\experiments\\measurements"; //TUF


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
	
/*
		//TECHNIQUE B
		int[] periods = {1000};
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
			service_times_distribution,
			useStep
		);
		b0.run();
*/


/*
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
	*/

		//TECHNIQUE C
		int coolofftime=1000;
		int[][] stepinfo= {{0,500,3000},{40,300,700},{50,150,300},{100,100,200},{200,0,0}};
		CAdaptiveTechnique ca = new CAdaptiveTechnique(
				isVerbose,
				runtimes,
				resultsFolderPath,
				application_location,
				inter_arrival_times,
				inter_arrival_times_distribution,
				handler_location,
				coolofftime,
				stepinfo,
				manager_location,
				service_times,
				service_times_distribution
		);
		ca.run();
		
	}//end main
}//end class
