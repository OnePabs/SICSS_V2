package Experiment_Controller;

import common.*;
import scripts.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		String experimentName = "technique-a-c0-20min";

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		experimentName = dtf.format(now) + "-" + experimentName;

		String resultsFolderPath;
		resultsFolderPath = "/home/ubuntu/experiment_results/"; //linux
		//resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"; //laptop
		//resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results\\measurements"; //desktop
		//resultsFolderPath = "C:\\Users\\juanp\\OneDrive\\Documents\\experiment_results\\"; //TUF

		String resultsPath = resultsFolderPath + experimentName;

		/*
		TestBConstant tb = new TestBConstant();
		tb.run();
		 */
/*
		TestCConstant tc = new TestCConstant(resultsPath);
		tc.run();
 */



		boolean isVerbose = false;
		
		String application_location = "http://ec2-3-138-174-172.us-east-2.compute.amazonaws.com:80";
		//String application_location = "http://localhost:8000";
		String handler_location = "http://ec2-18-116-45-199.us-east-2.compute.amazonaws.com:80";
		//String handler_location = "http://localhost:8080";
		String manager_location = "http://ec2-18-217-25-51.us-east-2.compute.amazonaws.com:80";
		//String manager_location = "http://localhost:8090";

		//RUNTIMES
		long minutes_to_millis = (long)60000;
		long[] runtimes = {20*minutes_to_millis};

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
		boolean useStep=false;


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

		//TECHNIQUE C
		int[] maxperiods = {30000};
		int[] maxsizes = {0};
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

/*
		//adaptive
		long cycletime = 10*minutes_to_millis; //milliseconds
		int coolofftime = 1000; //milliseconds
		CAdaptiveTechnique ca = new CAdaptiveTechnique(
				isVerbose,
				runtimes,
				resultsPath,
				application_location,
				inter_arrival_times,
				inter_arrival_times_distribution,
				cycletime,
				handler_location,
				coolofftime,
				manager_location,
				service_times,
				service_times_distribution,
				useStep
		);
		ca.run();
 */
		
	}//end main
}//end class
