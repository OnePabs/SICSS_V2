package scripts;

import common.ApplicationInterface;
import common.JsonToCsv;
import common.StorageAPIInterface;

import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


import java.util.LinkedList;
import java.util.Set;

/**
 * @author juancontreras
 *
 */
public class ApplicationStorageManagerTest extends ParentScript{
    private Long interArrivalTime;
    private Long[] runTimes;
    private int runTimeConversionFactorToMillis; //conversion factor to milliseconds
    private String applicationAddress;
    private String storageManagerAddress;
    private String resultsFolderPath;
    private boolean isVerbose;
    private int interOperationWaitTime = 1000;




    /**
     * @param interArrivalTime: in milliseconds
     * @param serviceTime: in milliseconds
     * @param runTimes: experiment run times
     * @param runTimeConversionFactorToMillis: conversion factor between runTimes units and milliseconds ex if runTimes is in seconds then runTimeConversionFactorToMillis=1000
     */
    public ApplicationStorageManagerTest(
                        Long interArrivalTime,
                         Long[] runTimes,
                         int runTimeConversionFactorToMillis,
                         String applicationAddress,
                         String storageManagerAddress,
                         boolean isVerbose
    ) {
        this.interArrivalTime = interArrivalTime;
        this.runTimes = runTimes.clone();
        this.runTimeConversionFactorToMillis = runTimeConversionFactorToMillis;
        this.applicationAddress = applicationAddress;
        this.storageManagerAddress = storageManagerAddress;
        this.isVerbose = isVerbose;
    }


    @Override
    public void run() {
        System.out.println("Script: Application Storage Manager test");
        System.out.println("Test Application and Storage Manager alone");
        System.out.println("Inter Arrival times distribution: Constant");
        System.out.println("Inter Arrival time: " + interArrivalTime);

        //Application Set up
        String jr2 = "{"
                + "\"receiverAddress\":\""+storageManagerAddress+"\","
                + "\"isVerbose\":" + String.valueOf(isVerbose) + ","
                +"\"useSleepForMockProcessing\":true,"
                +"\"interGenerationTimeDistribution\":\"CONSTANT\","
                +"\"interGenerationTimeDistributionSettings\":"+interArrivalTime
                + "}";

        ApplicationInterface ai = new ApplicationInterface(applicationAddress);
        ai.changeSettings(jr2);
        try {
            Thread.sleep(interOperationWaitTime);
        }catch(Exception e) {
            e.printStackTrace();
        }

        //RUN experiment
        try {
            for(int i=0; i<runTimes.length;i++){
                //Start Application
                if(isVerbose) {
                    System.out.println("Starting application");
                }
                ai.start();

                //wait for experiment runtime
                try {
                    Thread.sleep(runTimes[i]*runTimeConversionFactorToMillis);
                }catch(Exception e) {
                    e.printStackTrace();
                }

                //stop application
                if(isVerbose) {
                    System.out.println("Stopping application");
                }
                ai.stop();
                try {
                    Thread.sleep(interOperationWaitTime);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }

        }catch(Exception e) {
            if(isVerbose) {
                System.out.println("TestAppStrgManagerConstant not complete experiment");
            }
            e.printStackTrace();
        }
    }

}
