package StorageManager.service_time_creators;


import StorageManager.SettingsController;
import StorageManager.service_time_creators.creators.*;

public class ServiceTimeCreatorBuilder {
    public static ParentServiceTimeCreator build(SettingsController settingsController){
        String distributionStr = settingsController.getSetting("serviceTimeDistribution").toString();

        ParentServiceTimeCreator stc;
        switch (distributionStr){
            case "CONSTANT":
                stc = new ConstantServiceTimeCreator();
                break;
            case "EXPONENTIAL":
                stc = new ExponentialServiceTimeCreator();
                break;
            default:
                stc = new ParentServiceTimeCreator();
        }
        stc.setSettingsController(settingsController);
        stc.init();
        return stc;
    }
}
