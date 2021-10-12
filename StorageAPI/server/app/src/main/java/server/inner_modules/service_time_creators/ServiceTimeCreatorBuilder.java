package server.inner_modules.service_time_creators;

import server.enumerators.DISTRIBUTION;
import server.inner_modules.SettingsController;
import server.inner_modules.service_time_creators.creators.ConstantServiceTimeCreator;

public class ServiceTimeCreatorBuilder {
    public static ParentServiceTimeCreator build(SettingsController settingsController){
        String distributionStr = settingsController.getSetting("serviceTimeDistribution").toString();
        DISTRIBUTION distribution = DISTRIBUTION.valueOf(distributionStr);

        ParentServiceTimeCreator stc;
        switch (distribution){
            case CONSTANT:
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
