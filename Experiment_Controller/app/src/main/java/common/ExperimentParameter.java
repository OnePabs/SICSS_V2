package common;

import java.util.LinkedList;

public class ExperimentParameter {
    public String experimentName;
    public long experimentRuntime;
    public ApplicationInterface applications_interface;
    public String applications_parameters;
    public StorageAPIInterface storageApis_interface;
    public String storageApi_parameters;
    public StorageManagerInterface storageManager_interface;
    public String storageManager_parameters;

    public ExperimentParameter(
        String experimentName,
        long experimentRuntime, //in milliseconds
        String application_address,
        String applications_parameters,
        String storageApis_access_address,
        String storageApi_parameters,
        String storageManager_access_address,
        String storageManager_parameters
    ){
        this.experimentName = experimentName;
        this.experimentRuntime = experimentRuntime;
        this.applications_interface = new ApplicationInterface(application_address);
        this.applications_parameters = applications_parameters;
        this.storageApis_interface = new StorageAPIInterface(storageApis_access_address);
        this.storageApi_parameters = storageApi_parameters;
        this.storageManager_interface = new StorageManagerInterface(storageManager_access_address);
        this.storageManager_parameters = storageManager_parameters;
    }

    public void print(){
        System.out.println("Experiment ID: " + experimentName);

        System.out.print("Application address: ");
        System.out.print(applications_interface.address + ", ");
        System.out.println();


        System.out.println("Application parameters: " + applications_parameters);


        System.out.println("storage api access address: " + storageApis_interface.address);

        System.out.println("Storage Api parameters: " + storageApi_parameters);

        System.out.println("storage Manager access address: " + storageManager_interface.address);

        System.out.println("storage Manager parameters: " + storageManager_parameters);
    }
}