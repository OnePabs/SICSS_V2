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
    public void createServiceTime(int num_bytes){
        double numgen = Math.log(1-Math.random())*(-mean);
        long num = (long)numgen;
        if(settingsController.getIsVerbose()){
            System.out.println("meanInterArrivalTime: " + mean);
            System.out.println("double Generated: " + numgen);
            System.out.println("long Generated: " + num);
        }

        long st;
        if(num_bytes<=150){
            st = num;
        }else if(num_bytes<=1000){
            st=(num*3)/2;
        }else if(num_bytes<=2000){
            st=num*2;
        }else if(num_bytes<=3000){
            st=(num*5)/2;
        }else{
            st=num*3;
        }
        createTimLapse(st);
    }
}
