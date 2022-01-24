package common;

import java.util.LinkedList;

public class ExperimentParameter {
    public String experimentName;
    public LinkedList<ApplicationInterface> applications_interfaces;
    public String applications_parameters;
    public StorageAPIInterface storageApis_interface;
    public String storageApi_parameters;
    public StorageManagerInterface storageManager_interface;

    public ExperimentParameter(
        String experimentName,
        String[] applications_addresses,
        String applications_parameters,
        String storageApis_access_address,
        String storageApi_parameters,
        String storageManager_access_address
    ){
        this.experimentName = experimentName;
        applications_interfaces = new LinkedList<ApplicationInterface>();
        for(String application_addresse: applications_addresses){
            applications_interfaces.add(new ApplicationInterface(application_addresse));
        }
        this.applications_parameters = applications_parameters;
        this.storageApis_interface = new StorageAPIInterface(storageApis_access_address);
        this.storageApi_parameters = storageApi_parameters;
        this.storageManager_interface = new StorageManagerInterface(storageManager_access_address);
    }

    public void print(){

        System.out.println("Experiment ID: " + experimentName);

        System.out.print("Application addresses: ");
        for(ApplicationInterface appInt:applications_interfaces){
             System.out.print(appInt.address + ", ");
        }
        System.out.println();


        System.out.println("Application parameters: " + applications_parameters);


        System.out.println("storage api access address: " + storageApis_interface.address);

        System.out.println("Storage Api parameters: " + storageApi_parameters);

        System.out.println("storage Manager access address: " + storageManager_interface.address);
    }
}