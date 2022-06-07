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
    public void createServiceTime(int num_requests){
        if(settingsController.getIsVerbose()){
            System.out.println("constantTimeMillis: " + constantTimeMillis);
        }

        if(settingsController.containsSetting("usestepservicetime")){
            boolean usestepservicetime = settingsController.getBoolean("usestepservicetime");
            if(usestepservicetime){
                long st=0;
                if(num_requests==1){
                    st = constantTimeMillis;
                }else if(num_requests <= 10){
                    st=(constantTimeMillis*3)/2;
                }else if(num_requests<=20){
                    st=constantTimeMillis*2;
                }else if(num_requests<=30){
                    st=(constantTimeMillis*5)/2;
                }else if(num_requests > 30){
                    st=constantTimeMillis*3;
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
        createTimLapse(constantTimeMillis);
    }
}
