package Experiment_Controller;

import common.*;
import scripts.*;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");

		/*
		TestBConstant tb = new TestBConstant();
		tb.run();
		 */

		TestCConstant tc = new TestCConstant();
		tc.run();
/*

		boolean isVerbose = true;
		
		//String application_location = "http://ec2-18-116-67-207.us-east-2.compute.amazonaws.com:80";
		String application_location = "http://localhost:8000";
		//String handler_location = "http://ec2-3-144-111-183.us-east-2.compute.amazonaws.com:80";
		String handler_location = "http://localhost:8080";
		//String manager_location = "http://ec2-3-141-196-31.us-east-2.compute.amazonaws.com:80";
		String manager_location = "http://localhost:8090";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {25000};//{45*minutes_to_millis};

		//ARRIVAL TIMES
		int[] inter_arrival_times = {5000};
		String inter_arrival_times_distribution;
		//inter_arrival_times_distribution = "CONSTANT";
		inter_arrival_times_distribution = "GEOMETRIC";


		//SERVICE TIMES
		int[] service_times = {4000};
		String service_times_distribution;
		//service_times_distribution = "CONSTANT";
		service_times_distribution = "EXPONENTIAL";
		boolean useStep=false;

		//RESULTS
		//String resultsFolderPath = "/home/ubuntu/results"; //linux
		//String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"; //laptop
		//String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results\\measurements"; //desktop
		String resultsFolderPath = "C:\\Users\\juanp\\OneDrive\\Documents\\experiments\\2022-05-18-b-exp-step-disabled-but-implemented"; //TUF

*/

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
		int[] periods = {2000};
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
			service_times_distribution,
			useStep
		);
		c0.run();
	*/
		
	}//end main
}//end class
