package StorageManager.service_time_creators.creators;

import StorageManager.service_time_creators.ParentServiceTimeCreator;

public class ExponentialServiceTimeCreator extends ParentServiceTimeCreator {
    private long mean;

    //constructor
    public ConstantServiceTimeCreator(){}

    //Methods that need override
    @Override
    public void init(){
        Object settings = this.settingsController.getSetting("serviceTimeDistributionSettings");
        String meanStr = settings.toString();
        mean = Long.valueOf(constantTimeMillisStr);
    }

    @Override
    public void createServiceTime(){
        double timed = Math.log(1-Math.random())/(-mean);
        long timel = (long)timed;
        createTimLapse(timel);
    }
}
