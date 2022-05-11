//Test
package scripts;

import common.*;
import java.io.File;

public class TestBConstant {
    System.out.println("Test B Constant");
    boolean isVerbose = false;

    String application_location = "http://ec2-3-133-153-208.us-east-2.compute.amazonaws.com:80";
    //String application_location = "http://localhost:8000";
    String handler_location = "http://ec2-3-139-81-140.us-east-2.compute.amazonaws.com:80";
    //String handler_location = "http://localhost:8080";
    String manager_location = "http://35.203.51.116:80";
    //String manager_location = "http://localhost:8090";

    //RUNTIMES
    long minutes_to_millis = (long)60000;
    long[] runtimes = {1*minutes_to_millis};

    //ARRIVAL TIMES
    int[] inter_arrival_times = {50};
    String inter_arrival_times_distribution;
    inter_arrival_times_distribution = "CONSTANT";

    //SERVICE TIMES
    int[] service_times = {40};
    String service_times_distribution;
    service_times_distribution = "CONSTANT";



    //Small experiment to wake up everything
    //Technique A
    ATechnique a0 = new ATechnique(
            isVerbose,
            {1*minutes_to_millis},
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


    //Technique B constant tests in order of document
    //TECHNIQUE B
    int[] periods = {120, };
    boolean useStep=false;
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

}