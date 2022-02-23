package scripts;

import common.ExperimentParameter;

import java.util.Arrays;
import java.util.LinkedList;

public class TechBExponentialStubSaturation extends ParentScript{
    private boolean isVerbose;
    private long runtime;
    private long meanServiceTime;
    private long[] periods;
    private String[] inter_arrival_times;
    private String[] application_locations;
    private String[] storageApi_locations;
    private String storageManager_location;
    private String result_folder_path;

    public TechBExponentialStubSaturation(
            boolean isVerbose,
            long runtime,       //in milliseconds
            long meanServiceTime, //in milliseconds
            long[] periods, //milliseconds
            String[] inter_arrival_times,
            String[] application_locations,
            String[] storageApi_locations,
            String storageManager_location,
            String result_folder_path
    ){
        this.isVerbose = isVerbose;
        this.runtime = runtime;
        this.meanServiceTime = meanServiceTime;
        this.periods = Arrays.copyOf(periods,periods.length);
        this.inter_arrival_times = Arrays.copyOf(inter_arrival_times, inter_arrival_times.length);
        this.application_locations = Arrays.copyOf(application_locations, application_locations.length);
        this.storageApi_locations = Arrays.copyOf(storageApi_locations, storageApi_locations.length);
        this.storageManager_location = storageManager_location;
        this.result_folder_path = result_folder_path;
    }


    @Override
    public void run() {
        try{
            //create parameters
            LinkedList<ExperimentParameter> experiment_parameters = new LinkedList<ExperimentParameter>();
            for(int application_address_idx = 0; application_address_idx<application_locations.length;application_address_idx++){
                for(int storageApi_idx=0;storageApi_idx<storageApi_locations.length;storageApi_idx++){
                    String storageApi_access_address = storageApi_locations[storageApi_idx];
                    for(int period_idx=0;period_idx<periods.length;period_idx++){
                        for(String inter_arrival_time:inter_arrival_times){

                            //set up application addresses
                            String[] applications_addresses = new String[application_address_idx+1];
                            for(int j=0;j<=application_address_idx;j++){
                                applications_addresses[j] = application_locations[j];
                            }

                            //application parameters
                            String application_parameters = "{"
                                    + "\"receiverAddress\":\""+storageApi_access_address+"/data"+"\","
                                    + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                    + "\"useSleepForMockProcessing\":false,"
                                    + "\"interGenerationTimeDistribution\":\"GEOMETRIC\","
                                    + "\"interGenerationTimeDistributionSettings\":"+inter_arrival_time
                                    + "}";

                            String storageApi_parameters = "{"
                                    + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                    + "\"dataTransferTechnique\":\"b\","
                                    + "\"dataTransferTechniqueSettings\":{\"period\":" + String.valueOf(periods[period_idx]) + "},"
                                    + "\"serviceTimeDistribution\":\"CONSTANT\","
                                    + "\"serviceTimeDistributionSettings\":"+ 0 +","
                                    +"\"useSleepForMockProcessing\":false,"
                                    + "\"transmitter\":\"StorageManagerTransmitter\","
                                    + "\"destination\":\"" + storageManager_location + "\""
                                    + "}";

                            String storageManager_parameters = "{"
                                    + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                    + "\"platform\":\"stub\","
                                    + "\"serviceTimeDistribution\":\"EXPONENTIAL\","
                                    + "\"serviceTimeDistributionSettings\":"+ String.valueOf(meanServiceTime) +","
                                    +"\"useSleepForMockProcessing\":false"
                                    + "}";

                            String experimentName = "B-ia"+String.valueOf(inter_arrival_time)+"-p"+String.valueOf(periods[period_idx]);
                            //String.valueOf(application_address_idx)+"-"+String.valueOf(storageApi_idx)+"-"+inter_arrival_time;
                            ExperimentParameter expara = new ExperimentParameter(
                                    experimentName,
                                    runtime,
                                    applications_addresses,
                                    application_parameters,
                                    storageApi_access_address,
                                    storageApi_parameters,
                                    storageManager_location,
                                    storageManager_parameters
                            );
                            experiment_parameters.add(expara);
                        }
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
