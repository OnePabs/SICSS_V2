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

    private long sample_start_time = 0;
    private long num_requests_in_sample = 0;
    private long coolofftime;

    private long period = 300;
    private long threshold = 300;
    private Object notifier;



    public TechniqueCAdaptive(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject
    ) {
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueCadaptive";
        notifier = new Object();
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

        return true;
    }


    @Override
    public void waitForDataTransferCondition() throws Exception{ //condition for sending ready IO requests
        //initialize first sample start time
        if(sample_start_time == 0){
            sample_start_time = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - sample_start_time > coolofftime && num_requests_in_sample>0){
            long average_inter_arrival_time = (System.currentTimeMillis() - sample_start_time)/num_requests_in_sample;
            if(average_inter_arrival_time < 75){
                period = 300;
                threshold = 300;
            }else{
                period = 100;
                threshold = 0;
            }
            //reset sample start time and number of requests in sample
            sample_start_time = System.currentTimeMillis();
            num_requests_in_sample = 0;
        }

        //create period and threshold task
        ThresholdTask thresholdTask = new ThresholdTask(buffer,threshold,notifier);
        Thread tthreashold = new Thread(thresholdTask);
        tthreashold.start();

        if(period != 0){
            PeriodTask periodTask = new PeriodTask(period,notifier);
            Thread tperiod = new Thread(periodTask);
            tperiod.start();
            //wait for notification
            synchronized (notifier){
                try{
                    notifier.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            //finish period and threshold tasks execution
            try{
                periodTask.finishExecution();
            }catch(Exception e){}

            try{
                thresholdTask.finishExecution();
            }catch(Exception e){}
            //wait until tasks finish
            tperiod.join();
            tthreashold.join();
        }else{
            synchronized (notifier){
                try{
                    notifier.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            tthreashold.join();
        }

        //update number of requests in buffer
        num_requests_in_sample += buffer.getNumberOfRequestsInBuffer();
    }
}