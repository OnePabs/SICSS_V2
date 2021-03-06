package scripts;

import common.ExperimentParameter;

import java.util.Arrays;
import java.util.LinkedList;

public class ATechnique extends ParentScript{
    private boolean isVerbose;
    private long[] runtimes;
    private String result_folder_path;

    //application
    private String application_location;
    private int[] inter_arrival_times;
    private String inter_arrival_times_distribution;

    //storage handler
    private String handler_location;

    //Storage manager
    private String manager_location;
    private int[] service_times;
    private String service_times_distribution;
    
    /**
    
    */
    public ATechnique(
            boolean isVerbose,
            long[] runtimes,       //in milliseconds
            String result_folder_path,
            String application_location,
            int[] inter_arrival_times, //in milliseconds
            String inter_arrival_times_distribution,
            String handler_location,
            String manager_location,
            int[] service_times,
            String service_times_distribution
    ){
        this.isVerbose = isVerbose;
        this.runtimes = Arrays.copyOf(runtimes,runtimes.length);
        this.result_folder_path = result_folder_path;

        //application
        this.application_location = application_location;
        this.inter_arrival_times = Arrays.copyOf(inter_arrival_times,inter_arrival_times.length);
        this.inter_arrival_times_distribution = inter_arrival_times_distribution;

        //Handler
        this.handler_location = handler_location;

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
                for(int inter_arrival_time: inter_arrival_times){
                    
                    for(int service_time: service_times){
                        //application parameters
                        String application_parameters = "{"
                            + "\"receiverAddress\":\"" + handler_location + "/data"+"\","
                            + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                            + "\"useSleepForMockProcessing\":false,"
                            + "\"interGenerationTimeDistribution\":\"" + inter_arrival_times_distribution + "\","
                            + "\"interGenerationTimeDistributionSettings\":"+ inter_arrival_time
                            + "}";


                        String storageApi_parameters = "{"
                            + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                            + "\"dataTransferTechnique\":\"a\","
                            + "\"transmitter\":\"StorageManagerTransmitter\","
                            + "\"destination\":\"" + manager_location + "\""
                            + "}";


                        String storageManager_parameters = "{"
                            + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                            + "\"platform\":\"stub\","
                            + "\"executorType\":\"single\","
                            + "\"serviceTimeDistribution\":\"" + service_times_distribution + "\","
                            + "\"serviceTimeDistributionSettings\":"+ String.valueOf(service_time) +","
                            +"\"useSleepForMockProcessing\":false"
                            + "}";

                        String experimentName = 
                            "A-"+
                            "rt-"+String.valueOf(runtime) +"-"+
                            "ia-"+String.valueOf(inter_arrival_time) +"-"+
                            "st-"+String.valueOf(service_time)
                            ;

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
