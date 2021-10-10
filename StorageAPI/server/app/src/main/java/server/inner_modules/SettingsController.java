package server.inner_modules;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;

import server.enumerators.PROGRAM_STATE;

public class SettingsController {
	private Hashtable<String,String> settings;
	private StateController stateCtrl;
	public Object changaOfSettingsNotifier;

	public SettingsController() {
		settings = new Hashtable<String,String>();
		changaOfSettingsNotifier = new Object();
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
	public boolean changeSettings(Hashtable<String,String> newSettings) {
		if(stateCtrl.changeState(PROGRAM_STATE.SETTINGS)) {
			synchronized (changaOfSettingsNotifier){
				this.settings = newSettings;
				changaOfSettingsNotifier.notifyAll();
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
			JSONObject obj = (JSONObject)parser.parse(jsonstr);

			//Copy JSON settings into HashTable settings
			Hashtable<String,String> newSettings = new Hashtable<String,String>();
			Set<Object> keys = obj.keySet();
			for(Object key : keys){
				newSettings.put(String.valueOf(key),String.valueOf(obj.get(key)));
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
	public String getSetting(String settingName){
		return this.settings.get(settingName);
	}

	public int getNumberOfSettings(){
		return this.settings.size();
	}

	public boolean containsSetting(String settingName){
		return this.settings.containsKey(settingName);
	}

	public boolean getIsVerbose(){
		String settingName = "isVerbose";
		String settingValue;
		boolean isVerbose;
		if(containsSetting(settingName)){
			settingValue = getSetting(settingName);
			isVerbose = Boolean.valueOf(settingValue);
		}else{
			isVerbose = false;
		}
		return isVerbose;
	}
}
