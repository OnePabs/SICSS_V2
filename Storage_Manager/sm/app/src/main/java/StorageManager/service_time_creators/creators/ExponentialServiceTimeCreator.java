package StorageManager.service_time_creators.creators;

import StorageManager.service_time_creators.ParentServiceTimeCreator;

public class ExponentialServiceTimeCreator extends ParentServiceTimeCreator {
    private long mean;

    //constructor
    public ExponentialServiceTimeCreator(){}

    //Methods that need override
    @Override
    public void init(){
        Object settings = this.settingsController.getSetting("serviceTimeDistributionSettings");
        String meanStr = settings.toString();
        mean = Long.valueOf(meanStr);
    }

    @Override
    public void createServiceTime(int num_requests){
        double numgen = Math.log(1-Math.random())*(-mean);
        long num = (long)numgen;
        if(settingsController.getIsVerbose()){
            System.out.println("meanServiceTime: " + mean);
            System.out.println("double Generated: " + numgen);
            System.out.println("long Generated: " + num);
        }

        if(settingsController.containsSetting("usestepservicetime")){

            boolean usestepservicetime = settingsController.getBoolean("usestepservicetime");
            if(usestepservicetime){
                long st=0;
                if(num_requests==1){
                    st = num;
                }else if(num_requests<=10){
                    st=(num*3)/2;
                }else if(num_requests<=20){
                    st=num*2;
                }else if(num_requests<=30){
                    st=(num*5)/2;
                }else if(num_requests > 30){
                    st=num*3;
                }

                if(settingsController.getIsVerbose()){
                    System.out.println("usestepservicetime: true");
                    System.out.println("final service time created: " + st);
                    System.out.println();
                }

                createTimLapse(st);
                return;
            }
        }
        if(settingsController.getIsVerbose()){
            System.out.println("usestepservicetime: false");
            System.out.println();
        }
        createTimLapse(num);
    }
}
