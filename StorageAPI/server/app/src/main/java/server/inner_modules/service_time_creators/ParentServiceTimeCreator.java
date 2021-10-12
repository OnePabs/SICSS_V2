package server.inner_modules.service_time_creators;

import server.inner_modules.SettingsController;

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
    public void createServiceTime(){}

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
            try{
                Thread.sleep(numMillis);
            }catch(Exception e){
                if(settingsController.getIsVerbose()){
                    e.printStackTrace();
                }
            }
        }else{
            //create elapsed time by doing calculations
            long number = 12345;
            int numfactors = 0;
            int result;
            long startT = System.currentTimeMillis();
            do{
                for(int i=1;i<number;i++){
                    for(int j=1;j<12345;j++){
                        result = i*j;
                        if(result == number){
                            numfactors++;
                        }
                    }
                }
            }while( (System.currentTimeMillis()-startT)<numMillis );
            if(numfactors!=7 && settingsController.getIsVerbose()){
                System.out.println("createTimLapse did not find the correct factors of 12345. number of factors found: " + numfactors);
            }
        }
    }

}
