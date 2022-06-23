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
    private long startTime = 0;
    private long prev_arrival_time = 0;
    private long[] inter_arrival_times = new long[]{0,0,0,0,0,0,0,0,0,0}; //10 places
    private int tail = 0;
    private long period = 1000;
    private long threshold = 1000;
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
        if(startTime == 0){
            startTime = System.currentTimeMillis();
        }

        //update array of inter arrival times
        if(prev_arrival_time == 0){
            prev_arrival_time = System.currentTimeMillis();
        }else{
            inter_arrival_times[tail++%inter_arrival_times.length] = System.currentTimeMillis() - prev_arrival_time;
        }

        if(!isThereZeroInInterArrivals() && System.currentTimeMillis() - startTime > coolofftime){
            //see if period and threshold need to be changed
            long sum_inter_arrival_times = 0;
            for(long inter_arrival_time: inter_arrival_times){
                sum_inter_arrival_times += inter_arrival_time;
            }
            long average_inter_arrival_time = sum_inter_arrival_times / inter_arrival_times.length;

            if(average_inter_arrival_time < 45){
                period = 400;
                threshold = 400;
            }else if (average_inter_arrival_time < 55){
                period = 300;
                threshold = 300;
            }else if (average_inter_arrival_time < 110){
                period = 200;
                threshold = 200;
            }else{
                period = 0;
                threshold = 100;
            }
            startTime = System.currentTimeMillis();
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
    }


    private boolean isThereZeroInInterArrivals(){
        for(long inter_arrival_time: inter_arrival_times){
            if(inter_arrival_time==0){
                return true;
            }
        }
        return false;
    }


}