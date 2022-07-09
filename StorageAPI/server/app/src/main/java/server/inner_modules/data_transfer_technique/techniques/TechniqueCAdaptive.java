package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import server.Task;
import server.JsonAPI;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Hashtable;

public class TechniqueCAdaptive extends ParentDataTransferTechnique {

    private long coolofftime;

    private long period = 60000;
    private long threshold = 100;
    private MeasurementController measurementController;
    private ArrivalMonitorAndSettingsChanger arrivalMonitorAndSettingsChanger;
    private Object changeTechniqueSettingsLock;



    public TechniqueCAdaptive(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject,
            MeasurementController measurementController
    ) {
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueCadaptive";
        this.measurementController = measurementController;
        changeTechniqueSettingsLock = new Object();
    }


    @Override
    public boolean initialize(){
        if(settingsController.getIsVerbose()){
            System.out.println("C Adaptive Initialization");
        }
        if(!settingsController.containsSetting("coolofftime")){
            return false;
        }

        coolofftime = (long)settingsController.getSetting("coolofftime");
        if(settingsController.getIsVerbose()){
            System.out.println("coolofftime: " + coolofftime);
        }

        arrivalMonitorAndSettingsChanger = new ArrivalMonitorAndSettingsChanger(this,measurementController,coolofftime);
        Thread arrivalMonitorAndSettingsChangerThread = new Thread(arrivalMonitorAndSettingsChanger);
        arrivalMonitorAndSettingsChangerThread.start();

        return true;
    }

    public void changeThreshold(long threshold){
        synchronized (changeTechniqueSettingsLock){
            this.threshold = threshold;
        }
    }


    @Override
    public void waitForDataTransferCondition() throws Exception{ //condition for sending ready IO requests
        /*
        System.out.println("wainting for data transfer condition...");
        synchronized (changeTechniqueSettingsLock){
            try {
                Thread.sleep(5000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         */
        //System.out.println("Technique C Adaptive: Threshold = " + threshold);
        //long start_t = System.currentTimeMillis();

        //create lock
        Object lock = new Object();

        //create period and threshold task
        CPeriodTask periodTask = new CPeriodTask(period,lock);
        ThresholdMonitoringTask thresholdTask = new ThresholdMonitoringTask(threshold,buffer,lock);

        //link tasks together
        thresholdTask.setPeriodTask(periodTask);
        periodTask.setThresholdTask(thresholdTask);

        //create threads to run the tasks
        Thread tperiod = new Thread(periodTask);
        Thread tthreashold = new Thread(thresholdTask);

        //start the tasks
        tperiod.start();
        tthreashold.start();

        //wait until tasks finish
        tperiod.join();
        tthreashold.join();

        //long duration = System.currentTimeMillis() - start_t;
        //System.out.println("Technique C Adaptive finished waiting for " + duration + " milliseconds");
    }
}