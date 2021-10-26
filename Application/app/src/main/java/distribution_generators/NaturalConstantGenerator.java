package distribution_generators;

import Application.SettingsController;

public class NaturalConstantGenerator extends ParentNaturalNumberGenerator{
    private long constant;

    public NaturalConstantGenerator(SettingsController settingsController){
        super(settingsController);
    }

    @Override
    public boolean initialize() {
        //Object distributionSettings = this.settingsController.getSetting("interGenerationTimeDistributionSettings");
        //String constantStr = distributionSettings.toString();
        if(settingsController.containsSetting("interGenerationTimeDistributionSettings")){
            System.out.println("contains constant milliseconds. value is: " + settingsController.getSetting("interGenerationTimeDistributionSettings").toString());

        }else{
            System.out.println("settings do not contain constant value");
        }
        this.constant = (long)this.settingsController.getSetting("interGenerationTimeDistributionSettings");
        //Long.valueOf(constantStr);
        return true;
    }

    @Override
    public long generate() {
        return constant;
    }
}
