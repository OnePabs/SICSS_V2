package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.inner_modules.*;
import server.inner_modules.data_transfer_technique.ParentDataTransferTechnique;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class TechniqueCAdaptive extends ParentDataTransferTechnique {
    private int coolofftime;
    private JSONArray stepinfo;
    public TechniqueCAdaptive(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists buffer,
            TransmitionInformationObject transmitionInformationObject
    ) {
        super(stateController, settingsController, buffer, transmitionInformationObject);
        techniqueName = "techniqueCadaptive";
    }


    /*
     *Initialize technique
     * Parameters needed from settingsController
     * {... dataTransferTechniqueSettings: { coolofftime:Int(milliseconds), stepvaluesandcparameters:[[iastepmin:Int,period:Int,threshold:Int],[...],...] } ... }
     * coolofftime = Minimum time in between changes of period and threshold
     * stepvaluesandcparameters = represents the steps/min inter arrival values at which the period and threshold must be changed
     * iastepmin: The minimum value after which the period and threshold must be changed
     * period: the period that the adaptive technique will change to after iastepmin has been reached
     * threshold: the threshold that the adaptive technique will change to after iastepmin has been reached
     *
     * Example:
     * dataTransferTechniqueSettings: {coolofftime:5000, stepvaluesandcparameters: [ [0,500,30000],[40,300,700],[50,150,300],[100,100,200],[200,0,0] ] }
     * explanation:
     * The adaptive technique will check every 5000 milliseconds wheter or not the average inter arrival rate has changed from one step to another
     * the steps are as follow:
     * if the average inter arrival rate computed over the cooloff period is from 0 milliseconds to 39 milliseconds then the period set is 500 and the threshold 30000
     * if the average inter arrival rate computed over the cooloff period is from 40 milliseconds to 49 milliseconds then the period set is 300 and the threshold 700
     * if the average inter arrival rate computed over the cooloff period is from 50 milliseconds to 99 milliseconds then the period set is 150 and the threshold 300
     * if the average inter arrival rate computed over the cooloff period is from 100 milliseconds to 199 milliseconds then the period set is 100 and the threshold 200
     * if the average inter arrival rate computed over the cooloff period is over 200 milliseconds then the period set is 0 and the threshold 0
     *
     * if the first iastepmin is not zero, then when the average inter arrival time is less than the first iastepmin the adaptive technique will
     * use the default P=1000 and T=60000
     */
    @Override
    public boolean initialize(){
        if(!settingsController.containsSetting("dataTransferTechniqueSettings")){
            return false;
        }
        Hashtable<String,Object> dtSettings = JsonAPI.jsonToHashTable(settingsController.getSetting("dataTransferTechniqueSettings"));
        if(!dtSettings.containsKey("stepvaluesandcparameters") || !dtSettings.containsKey("coolofftime") ){
            return false;
        }
        coolofftime = dtSettings.get("coolofftime")
        stepinfo = JsonAPI.objectToJSONArray(dtSettings.get("stepvaluesandcparameters"))

        if(settingsController.getIsVerbose()){
            System.out.println("Technique C Adaptive coolofftime:" coolofftime + ", step definition: " + stepinfo.toString());
        }
        return true;
    }
}