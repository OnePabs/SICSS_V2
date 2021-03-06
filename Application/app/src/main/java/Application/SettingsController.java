package Application;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.Hashtable;
import java.util.Set;

import enumerators.PROGRAM_STATE;

public class SettingsController {
    private Hashtable<String,Object> settings;
    private StateController stateCtrl;
    public Object changeOfSettingsNotifier;

    public SettingsController() {
        settings = new Hashtable<String,Object>();
        changeOfSettingsNotifier = new Object();
    }

    public void setStateController(StateController stateCtrl){
        this.stateCtrl = stateCtrl;
    }

    /*
     * @params newSettings is a Hashtable with all the settings for the Storage API as Strings
     * Changes the Storage API state to SETTINGS. Notifies all threads waiting on changaOfSettingsNotifier
     * Returns true if the Storage API state changed to SETTING and new settings are set
     * Returns false otherwise
     * */
    public boolean changeSettings(Hashtable<String,Object> newSettings) {
        if(stateCtrl.changeState(PROGRAM_STATE.SETTINGS)) {
            synchronized (changeOfSettingsNotifier){
                this.settings = newSettings;
                changeOfSettingsNotifier.notifyAll();
            }
            return true;
        }
        return false;
    }

    /*
     * @Params jsonstr: JSON string representing all the settings. start and end with curly brackets. only settingName:settingValue allowed
     * returns: True if parsing was successful and new settings were created. False otherwise.
     * */
    public boolean changeSettings(String jsonstr){
        JSONParser parser = new JSONParser();
        try{
            //parse JSON string into JSON object {key:value,...}
            JSONObject jasonObject = (JSONObject)parser.parse(jsonstr);

            //Copy JSON settings into HashTable settings
            Hashtable<String,Object> newSettings = new Hashtable<String,Object>();
            Set<Object> keys = jasonObject.keySet();
            for(Object key : keys){
                newSettings.put(String.valueOf(key),jasonObject.get(key));
            }
            return changeSettings(newSettings);
        }catch(ParseException pe) {
            System.out.println("Error Changing Settings from JSON String: Parser Exception. json string="+jsonstr);
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
            return false;
        }
    }

    /*
     * @Params settingName: setting name
     * returns: setting value if setting name was found. null String otherwise
     * */
    public Object getSetting(String settingName){
        return this.settings.get(settingName);
    }

    public boolean getBoolean(String settingName){
        Object settingValue;
        boolean b;
        if(containsSetting(settingName)){
            settingValue = getSetting(settingName);
            b = Boolean.valueOf(settingValue.toString());
        }else{
            b = false;
        }
        return b;
    }

    public int getNumberOfSettings(){
        return this.settings.size();
    }

    public boolean containsSetting(String settingName){
        return this.settings.containsKey(settingName);
    }

    public boolean getIsVerbose(){
        String settingName = "isVerbose";
        Object settingValue;
        boolean isVerbose;
        if(containsSetting(settingName)){
            settingValue = getSetting(settingName);
            isVerbose = Boolean.valueOf(settingValue.toString());
        }else{
            isVerbose = false;
        }
        return isVerbose;
    }
}
