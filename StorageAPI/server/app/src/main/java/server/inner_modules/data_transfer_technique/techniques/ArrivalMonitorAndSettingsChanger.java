package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import server.enumerators.*;

public class ArrivalMonitorAndSettingsChanger implements Runnable{
    private MeasurementController measurementController;
    private TechniqueCAdaptive techniqueCAdaptive;
    private long coolofftime;

    public ArrivalMonitorAndSettingsChanger(TechniqueCAdaptive techniqueCAdaptive,
                                            MeasurementController measurementController,
                                            long coolofftime){
        this.measurementController=measurementController;
        this.techniqueCAdaptive = techniqueCAdaptive;
        this.coolofftime = coolofftime;
    }

    public void run(){
        while(true){
            if(measurementController!=null && techniqueCAdaptive!=null){
                //System.out.println("ArrivalMonitorAndSettingsChanger: checks for change in Inter Arrival Time");
                //System.out.println("ArrivalMonitorAndSettingsChanger: changing settings... : " + System.currentTimeMillis());
                int n = 100; //look at the past 100 time stamps. this does not mean 100 request arrival times since each request has 9 timestamps
                TimeStamp[] allMeasurements = measurementController.getMeasurements();
                if(allMeasurements.length >= n){
                    //System.out.println("ArrivalMonitorAndSettingsChanger: Number of measurements = " + allMeasurements.length);
                    TimeStamp currTimeStamp;
                    long sum = 0;
                    long prevEntryTime = 0;
                    long numIa = 0;
                    for(int i=1;i<=n;i++){
                        currTimeStamp = allMeasurements[allMeasurements.length - i];
                        if(currTimeStamp.getName() == TIMESTAMP_NAME.STORAGE_API_ENTRY){
                            if(prevEntryTime==0){
                                prevEntryTime = currTimeStamp.getTimeStamp();
                            }else{
                                long currIa = prevEntryTime - currTimeStamp.getTimeStamp();
                                //System.out.println("current Ia: " + currIa);
                                sum += currIa;
                                prevEntryTime = currTimeStamp.getTimeStamp();
                                numIa++;
                            }
                        }
                    }
                    sum = sum/1000000;
                    //System.out.println("sum: " + sum + ", num_ia: " + numIa);
                    long averageIa =  sum/numIa;
                    long threshold = 100;
                    if(averageIa < 75){
                        threshold = 300;
                    }
                    //System.out.println("ArrivalMonitorAndSettingsChanger average ia:" + averageIa + ", changing THRESHOLD value to: " + threshold);
                    techniqueCAdaptive.changeThreshold(threshold);
                }

                //cool off time
                //System.out.println("ArrivalMonitorAndSettingsChanger: changed settings - " + System.currentTimeMillis());
                try {
                    Thread.sleep(coolofftime);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //System.out.println("ArrivalMonitorAndSettingsChanger: finished cool off" + System.currentTimeMillis());


            }else{
                try {
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
