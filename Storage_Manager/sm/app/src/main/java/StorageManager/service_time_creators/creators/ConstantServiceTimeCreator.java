package StorageManager.service_time_creators.creators;

import StorageManager.service_time_creators.ParentServiceTimeCreator;

public class ConstantServiceTimeCreator extends ParentServiceTimeCreator {
    private long constantTimeMillis;

    //constructor
    public ConstantServiceTimeCreator(){}

    //Methods that need override
    @Override
    public void init(){
        Object settings = this.settingsController.getSetting("serviceTimeDistributionSettings");
        String constantTimeMillisStr = settings.toString();
        constantTimeMillis = Long.valueOf(constantTimeMillisStr);
    }

    @Override
    public void createServiceTime(){
        createTimLapse(constantTimeMillis);
    }
}
