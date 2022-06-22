package distribution_generators;

import Application.SettingsController;
import Application.StateController;

public abstract class ParentNaturalNumberGenerator {
    protected SettingsController settingsController;

    public ParentNaturalNumberGenerator(SettingsController settingsController){
        this.settingsController = settingsController;
    }

    public abstract boolean initialize();
    public abstract long generate();
    public abstract long generate(long num);
}
