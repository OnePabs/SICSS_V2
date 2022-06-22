package distribution_generators;

import java.util.Hashtable;
import java.util.Random;

import Application.SettingsController;
import Application.JsonAPI;


public class NaturalGeometricGenerator extends ParentNaturalNumberGenerator{
    //private Hashtable<String,Object> generatorSettings;
    private Long mean;

    public NaturalGeometricGenerator(SettingsController settingsController){
        super(settingsController);
    }

    @Override
    public boolean initialize() {
        try{
            String lambdaStr = settingsController.getSetting("interGenerationTimeDistributionSettings").toString();
            mean = Long.valueOf(lambdaStr);
            System.out.println("mean: " + mean);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long generate() {
        //System.out.println("mean: " + mean);
        double numgen = Math.log(1-Math.random())*(-mean);
        //System.out.println("double Generated: " + numgen);
        long num = (long)numgen;
        //System.out.println("long Generated: " + num);
        return  num;
    }

    @Override
    public long generate(long num){
        //System.out.println("mean: " + mean);
        double numgen = Math.log(1-Math.random())*(-num);
        //System.out.println("double Generated: " + numgen);
        return (long)numgen;
    }
}
