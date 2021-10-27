package distribution_generators;

import java.util.Hashtable;
import java.util.Random;

import Application.SettingsController;
import Application.JsonAPI;


public class NaturalGeometricGenerator extends ParentNaturalNumberGenerator{
    //private Hashtable<String,Object> generatorSettings;
    private Long lambda;
    private Random r;

    public NaturalGeometricGenerator(SettingsController settingsController){
        super(settingsController);
    }

    @Override
    public boolean initialize() {
        try{
            Object settingsObj = settingsController.getSetting("interGenerationTimeDistributionSettings");
            Hashtable<String,Object> generatorSettings = JsonAPI.jsonToHashTable(settingsObj);

            lambda = (Long)generatorSettings.get("lambda");

            long seed;
            if(generatorSettings.containsKey("seed")){
                seed = (Long)generatorSettings.get("seed");
            }else{
                seed = 0;
            }

            r = new Random(seed);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e + " ");
            return false;
        }
    }

    @Override
    public long generate() {
        return  Math.round(Math.log(1-r.nextDouble())/(-lambda));
    }
}
