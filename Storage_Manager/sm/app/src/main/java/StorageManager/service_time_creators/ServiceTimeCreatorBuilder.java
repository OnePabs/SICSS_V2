package StorageManager.service_time_creators;


import StorageManager.SettingsController;
import StorageManager.service_time_creators.creators.ConstantServiceTimeCreator;

public class ServiceTimeCreatorBuilder {
    public static ParentServiceTimeCreator build(SettingsController settingsController){
        String distributionStr = settingsController.getSetting("serviceTimeDistribution").toString();

        ParentServiceTimeCreator stc;
        switch (distributionStr){
            case "CONSTANT":
                stc = new ConstantServiceTimeCreator();
                break;
            default:
                stc = new ParentServiceTimeCreator();
        }
        stc.setSettingsController(settingsController);
        stc.init();
        return stc;
    }
}
