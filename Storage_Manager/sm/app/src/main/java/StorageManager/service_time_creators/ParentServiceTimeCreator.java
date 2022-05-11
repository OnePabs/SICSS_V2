package StorageManager.service_time_creators;

import StorageManager.SettingsController;

public class ParentServiceTimeCreator {
    protected SettingsController settingsController;

    //constructor
    public ParentServiceTimeCreator(){}

    //setter
    public void setSettingsController(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    //child must @Override these
    public void init(){}
    public void createServiceTime(int num_requests){
        System.out.println("Parent Service Time Creator createServiceTime()");
    }

    //child common methods
    public void createTimLapse(long numMillis){

        //check whether to use sleep on continuous loop
        boolean useSleepForMockProcessing = false;
        if(settingsController.containsSetting("useSleepForMockProcessing")){
            Object settingValue = settingsController.getSetting("useSleepForMockProcessing");
            useSleepForMockProcessing = Boolean.valueOf(settingValue.toString());
        }

        //create the timelapse
        if(useSleepForMockProcessing){
            long startTime = System.currentTimeMillis();
            try{
                Thread.sleep(numMillis);
            }catch(Exception e){
                if(settingsController.getIsVerbose()){
                    e.printStackTrace();
                }
            }
            if(settingsController.getIsVerbose()){
                long duration = System.currentTimeMillis() - startTime; 
                System.out.println("Service Time Creator: Performed service time of " + duration + " milliseconds");
            }
        }else{
            //create elapsed time by doing calculations
            long startTime = System.currentTimeMillis();
            do{
                /*
                int number = 10000;
                int currFactor = 0;
                for(int i=1;i<number;i++){
                    if(number%i == 0){
                        currFactor = i;
                    }
                }
                //long p = System.nanoTime() - startTime;
                //System.out.println("Time for one iteration of " +number+ " in nano: " + p);
                //System.exit(0);
                */
                try{
                    Thread.sleep(0,500000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while((System.currentTimeMillis()-startTime)<numMillis);
            if(settingsController.getIsVerbose()){
                long duration = System.currentTimeMillis() - startTime; 
                System.out.println("Service Time Creator: Performed service time of " + duration + " milliseconds");
            }
        }
    }

}
