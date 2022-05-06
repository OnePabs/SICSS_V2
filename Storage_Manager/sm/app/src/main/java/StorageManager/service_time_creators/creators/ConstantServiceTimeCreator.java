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
    public void createServiceTime(int num_bytes){
        if(settingsController.containsSetting("usestepservicetime")){
            boolean usestepservicetime = settingsController.getBoolean("usestepservicetime");
            if(usestepservicetime){
                long st;
                if(num_bytes<=150){
                    st = constantTimeMillis;
                }else if(num_bytes<=1000){
                    st=(constantTimeMillis*3)/2;
                }else if(num_bytes<=2000){
                    st=constantTimeMillis*2;
                }else if(num_bytes<=3000){
                    st=(constantTimeMillis*5)/2;
                }else{
                    st=constantTimeMillis*3;
                }
                createTimLapse(st);
                return;
            }
        }
        createTimLapse(constantTimeMillis);
    }
}
