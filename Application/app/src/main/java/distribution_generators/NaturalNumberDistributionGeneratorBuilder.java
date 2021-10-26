package distribution_generators;

import Application.SettingsController;
import Application.StateController;
import enumerators.DISTRIBUTION;

public class NaturalNumberDistributionGeneratorBuilder {
    public static ParentNaturalNumberGenerator build(
            SettingsController settingsController
    ){
        String distributionStr = settingsController.getSetting("interGenerationTimeDistribution").toString();
        DISTRIBUTION distribution = DISTRIBUTION.valueOf(distributionStr);
        ParentNaturalNumberGenerator generator;
        switch(distribution){
            case CONSTANT:
                generator = new NaturalConstantGenerator(settingsController);
                break;
            default:
                generator = null;
        }
        return generator;
    }
}
