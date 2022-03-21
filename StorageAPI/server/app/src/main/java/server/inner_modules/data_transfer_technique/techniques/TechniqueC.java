package server.inner_modules.data_transfer_technique.techniques;

import server.JsonAPI;
import server.Task;
import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;

import java.util.Hashtable;


public class TechniqueC extends ParentDataTransferTechnique {
    private Long period;
    private Long periodStartTime = null;
    private Long maxSize;
    private Long currentSize;

    public TechniqueC(
        StateController stateController, 
        SettingsController settingsController,
        ReadyLists buffer,
        TransmitionInformationObject transmitionInformationObject
    ){
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueC";
    }

    @Override
    public boolean initialize(){
        if(!settingsController.containsSetting("dataTransferTechniqueSettings")){
            return false;
        }
        Hashtable<String,Object> dtSettings = JsonAPI.jsonToHashTable(settingsController.getSetting("dataTransferTechniqueSettings"));
        if(!dtSettings.containsKey("period") || !dtSettings.containsKey("maxsize") ){
            return false;
        }
        period = (Long)dtSettings.get("period");
        periodStartTime = Long.valueOf(System.currentTimeMillis());
        maxSize = (Long)dtSettings.get("maxsize");
        currentSize = (long)0;
        if(settingsController.getIsVerbose()){
            System.out.println("Technique C maxsize:" + maxSize + " period: " + period);
        }
        return true;
    }



    @Override
    public void waitForDataTransferCondition() throws Exception{ //condition for sending ready IO requests

        //create lock
        Object lock = new Object();

        //create period and threshold task
        CPeriodTask periodTask = new CPeriodTask(period,lock);
        ThresholdMonitoringTask thresholdTask = new ThresholdMonitoringTask(maxSize,buffer,lock);

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
    }
}


/**
Task runs finishes after period of time
Task notifies threshold monitor to finish execution if this tasks finishes first
*/
class CPeriodTask extends Task {
    long period;
    boolean canExecute;
    ThresholdMonitoringTask ttask;
    Object lock;

    public CPeriodTask(long period, Object lock){
        this.period = period;
        this.canExecute = true;
        this.ttask = ttask;
        this.lock = lock;
    }

    public void setThresholdTask(ThresholdMonitoringTask ttask){
        this.ttask = ttask;
    }

    @Override
    public void run(){
        if(this.canExecute){
            //sleep for the period specified
            //stop sleeping if this thread receives the command to stop execution
            long start_time = System.currentTimeMillis();
            long duration = 0;
            while(duration < period && this.canExecute){
                try{
                    Thread.sleep(0,500000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    duration = System.currentTimeMillis() - start_time;
                }
            }
        }
        synchronized(lock){
            if(this.canExecute){
                try{
                    ttask.finishExecution();
                }catch(Exception e){}
            }
        }

    }

    public void finishExecution(){
        this.canExecute = false;
    }
}


/**
Task finished once the threshold is met or canExecute is set to false
Task notifies Period task to finish if this task finishes first
*/
class ThresholdMonitoringTask extends Task {
    long max_size;
    ReadyLists buffer;
    boolean canExecute;
    CPeriodTask cPeriodTask;
    Object lock;

    public ThresholdMonitoringTask(long max_size, ReadyLists buffer, Object lock){
        this.max_size = max_size;
        this.buffer = buffer;
        this.cPeriodTask = cPeriodTask;
        this.lock = lock;
        this.canExecute = true;
    }

    public void setPeriodTask(CPeriodTask cPeriodTask){
        this.cPeriodTask = cPeriodTask;
    }

    @Override
    public void run(){
        int numBytes = buffer.getNumberOfBytesInBuffer();
        while(numBytes < max_size && this.canExecute){
            //Poll buffer for number of bytes
            try{
                    Thread.sleep(0,500000);
                }catch(Exception e){
                    e.printStackTrace();
            }
            //update number of bytes in the buffer
            numBytes = buffer.getNumberOfBytesInBuffer();
        }
        synchronized(lock){
            if(this.canExecute){
                try{
                    cPeriodTask.finishExecution();
                }catch(Exception e){}
            }
        }
    }

    public void finishExecution(){
        this.canExecute = false;
    }
}
