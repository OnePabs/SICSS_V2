
package scripts;

import common.ExperimentParameter;

import java.util.Arrays;
import java.util.LinkedList;

public class CAdaptiveTechnique extends ParentScript{
    private boolean isVerbose;
    private long[] runtimes;
    private String result_folder_path;

    //application
    private String application_location;
    private int[] base_inter_arrival_times;
    private String inter_arrival_times_distribution;
    private long cycletime;

    //storage handler
    private String handler_location;
    private int coolofftime;
    private boolean usestepservicetime;

    //Storage manager
    private String manager_location;
    private int[] service_times;
    private String service_times_distribution;


    public CAdaptiveTechnique(
            boolean isVerbose,
            long[] runtimes,       //in milliseconds
            String result_folder_path,
            String application_location,
            int[] base_inter_arrival_times,
            String inter_arrival_times_distribution,
            long cycletime,
            String handler_location,
            int coolofftime,
            String manager_location,
            int[] service_times,
            String service_times_distribution,
            boolean usestepservicetime
    ){
        this.isVerbose = isVerbose;
        this.runtimes = Arrays.copyOf(runtimes,runtimes.length);
        this.result_folder_path = result_folder_path;

        //application
        this.application_location = application_location;
        this.base_inter_arrival_times = base_inter_arrival_times;
        this.inter_arrival_times_distribution = inter_arrival_times_distribution;
        this.cycletime = cycletime;

        //Handler
        this.handler_location = handler_location;
        this.coolofftime = coolofftime;


        //Manager
        this.manager_location = manager_location;
        this.service_times = Arrays.copyOf(service_times,service_times.length);
        this.service_times_distribution = service_times_distribution;
    }




    @Override
    public void run() {
        try{
            //create parameters
            LinkedList<ExperimentParameter> experiment_parameters = new LinkedList<ExperimentParameter>();

            for(long runtime:runtimes){
                for(int base_inter_arrival_time: base_inter_arrival_times) {
                    for (int service_time : service_times) {

                        //application parameters
                        String application_parameters = "{"
                                + "\"receiverAddress\":\"" + handler_location + "/data" + "\","
                                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                + "\"useSleepForMockProcessing\":false,"
                                + "\"useMultipleRates\":true,"
                                + "\"interGenerationTimeDistribution\":\"" + inter_arrival_times_distribution + "\","
                                + "\"interGenerationTimeDistributionSettings\":" + base_inter_arrival_time + ","
                                + "\"cycletime\":" + cycletime
                                + "}";

                        //Storage API parameters

                        //A
                        String storageApi_parameters = "{"
                                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                + "\"dataTransferTechnique\":\"a\","
                                + "\"transmitter\":\"StorageManagerTransmitter\","
                                + "\"destination\":\"" + manager_location + "\""
                                + "}";
/*
                        //B
                        String storageApi_parameters = "{"
                                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                + "\"dataTransferTechnique\":\"b\","
                                + "\"dataTransferTechniqueSettings\":{\"period\":150},"
                                + "\"transmitter\":\"StorageManagerTransmitter\","
                                + "\"destination\":\"" + manager_location + "\""
                                + "}";
 */
/*
                        //Adaptive
                        String storageApi_parameters = "{"
                                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                + "\"dataTransferTechnique\":\"ca\","
                                + "\"coolofftime\":" + String.valueOf(coolofftime) + ","
                                + "\"transmitter\":\"StorageManagerTransmitter\","
                                + "\"destination\":\"" + manager_location + "\""
                                + "}";
*/


                        String storageManager_parameters = "{"
                                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                + "\"platform\":\"stub\","
                                + "\"executorType\":\"multiple\","
                                + "\"serviceTimeDistribution\":\"" + service_times_distribution + "\","
                                + "\"serviceTimeDistributionSettings\":" + String.valueOf(service_time) + ","
                                + "\"useSleepForMockProcessing\":false"
                                + "\"usestepservicetime\"" + String.valueOf(usestepservicetime)
                                + "}";

                        String experimentName =
                                "ca-" +
                                        "rt-" + String.valueOf(runtime) + "-" +
                                        "ia-" + String.valueOf(base_inter_arrival_time) + "-" +
                                        "cy-" + String.valueOf(cycletime) + "-" +
                                        "cl-" + String.valueOf(coolofftime) + "-" +
                                        "st-" + String.valueOf(service_time);

                        ExperimentParameter expara = new ExperimentParameter(
                                experimentName,
                                runtime,
                                application_location,
                                application_parameters,
                                handler_location,
                                storageApi_parameters,
                                manager_location,
                                storageManager_parameters
                        );
                        experiment_parameters.add(expara);
                    }
                }

            }

            //run experiments
            RunExperimentScript.runExperiment(experiment_parameters,result_folder_path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
