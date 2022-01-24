package scripts;

import java.util.Arrays;
import java.util.LinkedList;
import common.*;

public class TechAConstantSaturation extends ParentScript{
    private boolean isVerbose;
    private long runtime;
    private String[] inter_arrival_times;
    private String[] application_locations;
    private String[] storageApi_locations;
    private String storageManager_location;
	private String result_folder_path;

    public TechAConstantSaturation(
        boolean isVerbose,
        long runtime,       //in minutes
        String[] inter_arrival_times,
        String[] application_locations,
        String[] storageApi_locations,
        String storageManager_location,
        String result_folder_path
    ){
        this.isVerbose = isVerbose;
        this.runtime = runtime;
        this.inter_arrival_times = Arrays.copyOf(inter_arrival_times, inter_arrival_times.length);
        this.application_locations = Arrays.copyOf(application_locations, application_locations.length);
        this.storageApi_locations = Arrays.copyOf(storageApi_locations, storageApi_locations.length);
        this.storageManager_location = storageManager_location;
        this.result_folder_path = result_folder_path;
    }


    @Override
	public void run() {

        //create parameters
        LinkedList<ExperimentParameter> experiment_parameters = new LinkedList<ExperimentParameter>();
        for(int application_address_idx = 0; application_address_idx<application_locations.length;application_address_idx++){
            for(int storageApi_idx=0;storageApi_idx<storageApi_locations.length;storageApi_idx++){
                String storageApi_access_address = storageApi_locations[storageApi_idx];
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
                        +"\"useSleepForMockProcessing\":true,"
                        +"\"interGenerationTimeDistribution\":\"CONSTANT\","
                        +"\"interGenerationTimeDistributionSettings\":"+inter_arrival_time
                        + "}";

                    String storageApi_parameters = "{"
                        + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                        + "\"dataTransferTechnique\":\"a\","
                        + "\"dataTransferTechniqueSettings\":{},"
                        + "\"serviceTimeDistribution\":\"CONSTANT\","
                        + "\"serviceTimeDistributionSettings\":"+ 0 +","
                        +"\"useSleepForMockProcessing\":true,"
                        + "\"transmitter\":\"StorageManagerTransmitter\","
                        + "\"destination\":\"" + storageManager_location + "\","
                        + "}";

                    String experimentName = "A-"+String.valueOf(application_address_idx)+"-"+String.valueOf(storageApi_idx)+"-"+inter_arrival_time;
                    ExperimentParameter expara = new ExperimentParameter(
                        experimentName,
                        applications_addresses,
                        application_parameters,
                        storageApi_access_address,
                        storageApi_parameters,
                        storageManager_location
                    );
                    experiment_parameters.add(expara);
                }
            }
        }

        //run experiments
        RunExperimentScript.runExperiment(experiment_parameters,result_folder_path);
    }
}