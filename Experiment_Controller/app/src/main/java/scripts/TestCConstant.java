//Test
package scripts;

import common.*;
import java.io.File;

public class TestCConstant {
    public void run() {
        System.out.println("Test C Constant");

        boolean isVerbose = false;

        //String application_location = "http://:80";
        String application_location = "http://localhost:8000";
        //String handler_location = "http://:80";
        String handler_location = "http://localhost:8080";
        //String manager_location = "http://:80";
        String manager_location = "http://localhost:8090";

        //RUNTIMES
        long minutes_to_millis = (long) 60000;
        long[] runtimes = {15 * minutes_to_millis};

        //ARRIVAL TIMES
        int[] inter_arrival_times = {50};
        String inter_arrival_times_distribution;
        inter_arrival_times_distribution = "CONSTANT";

        //SERVICE TIMES
        int[] service_times = {40};
        String service_times_distribution;
        service_times_distribution = "CONSTANT";
        boolean useStep = false;

        //RESULTS
        //String resultsFolderPath = "/home/ubuntu/results"; //linux
        //String resultsFolderPath = "C:\\Users\\Juan Pablo Contreras\\Documents\\expresults\\results"; //mini laptop
        //String resultsFolderPath = "C:\\Users\\juanp\\Documents\\experiment_results\\measurements"; //desktop
        String resultsFolderPath = "C:\\Users\\juanp\\OneDrive\\Documents\\experiments\\2022-05-18-tests\\measurements";


        //Small experiment to wake up everything
        //Technique A
        long[] wake_up_rt = {1*minutes_to_millis};
        ATechnique a0 = new ATechnique(
                isVerbose,
                wake_up_rt,
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


        //Technique C constant tests in order of document
        //TECHNIQUE B
        int[] maxperiods = {60000};
        int[] maxsizes = {0, 100, 200, 2000};
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

        //Technique C constant tests in order of document
        int[] maxsizes_step = {0, 100, 200, 1100,2100,3100};
        useStep = true;
        CTechnique c1 = new CTechnique(
                isVerbose,
                runtimes,
                resultsFolderPath,
                application_location,
                inter_arrival_times,
                inter_arrival_times_distribution,
                handler_location,
                maxperiods,
                maxsizes_step,
                manager_location,
                service_times,
                service_times_distribution,
                useStep
        );
        c1.run();
    }

}