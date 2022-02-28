package scripts;

import common.ExperimentParameter;

import java.util.Arrays;
import java.util.LinkedList;

public class TechCExponentialStubSaturation extends ParentScript{
    private boolean isVerbose;
    private long runtime;
    private long[] meanServiceTimes;
    private long maxperiod;
    private long[] maxsizes;
    private String[] inter_arrival_times;
    private String[] application_locations;
    private String[] storageApi_locations;
    private String storageManager_location;
    private String result_folder_path;


    public TechCExponentialStubSaturation(
            boolean isVerbose,
            long runtime,       //in milliseconds
            long[] meanServiceTimes, //in milliseconds
            long maxperiod, //milliseconds
            long[] maxsizes,
            String[] inter_arrival_times,
            String[] application_locations,
            String[] storageApi_locations,
            String storageManager_location,
            String result_folder_path
    ){
        this.isVerbose = isVerbose;
        this.runtime = runtime;
        this.meanServiceTimes = Arrays.copyOf(meanServiceTimes,meanServiceTimes.length);
        this.maxperiod = maxperiod;
        this.maxsizes = Arrays.copyOf(maxsizes,maxsizes.length);
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
                    for(int maxsize_idx=0;maxsize_idx<maxsizes.length;maxsize_idx++){
                        for(String inter_arrival_time:inter_arrival_times){
                            for(long meanServiceTime:meanServiceTimes){
                                //set up application addresses
                                String[] applications_addresses = new String[application_address_idx+1];
                                for(int j=0;j<=application_address_idx;j++){
                                    applications_addresses[j] = application_locations[j];
                                }

                                //application parameters
                                String application_parameters = "{"
                                        + "\"receiverAddress\":\""+storageApi_access_address+"/data"+"\","
                                        + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                        + "\"useSleepForMockProcessing\":true,"
                                        + "\"interGenerationTimeDistribution\":\"GEOMETRIC\","
                                        + "\"interGenerationTimeDistributionSettings\":"+inter_arrival_time
                                        + "}";

                                String storageApi_parameters = "{"
                                        + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                        + "\"dataTransferTechnique\":\"c\","
                                        + "\"dataTransferTechniqueSettings\":{\"period\":" + String.valueOf(maxperiod) + ","
                                        + "\"maxsize\":" + String.valueOf(maxsizes[maxsize_idx]) + "},"
                                        + "\"serviceTimeDistribution\":\"CONSTANT\","
                                        + "\"serviceTimeDistributionSettings\":"+ 0 +","
                                        +"\"useSleepForMockProcessing\":true,"
                                        + "\"transmitter\":\"StorageManagerTransmitter\","
                                        + "\"destination\":\"" + storageManager_location + "\""
                                        + "}";

                                String storageManager_parameters = "{"
                                        + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                                        + "\"platform\":\"stub\","
                                        + "\"serviceTimeDistribution\":\"EXPONENTIAL\","
                                        + "\"serviceTimeDistributionSettings\":"+ String.valueOf(meanServiceTime) +","
                                        +"\"useSleepForMockProcessing\":true"
                                        + "}";

                                String experimentName = 
                                "C-"+
                                "app-"+String.valueOf(application_address_idx)+"-"+
                                "api-"+String.valueOf(storageApi_idx)+"-"+
                                "ia-"+inter_arrival_time+"-"+
                                "mx-"+String.valueOf(maxsizes[maxsize_idx])+
                                "st-"+String.valueOf(meanServiceTime)
                                ;

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
            }

            //run experiments
            RunExperimentScript.runExperiment(experiment_parameters,result_folder_path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
