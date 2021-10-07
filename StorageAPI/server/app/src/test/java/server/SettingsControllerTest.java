package server;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;

import java.util.Hashtable;
import java.util.Iterator;

import java.io.IOException;
import java.io.StringWriter;

public class SettingsControllerTest {

    @Test public void testCreatingJSONobj(){

        System.out.println("Testing the creation of a JSON object and output of its string representation");
        System.out.println("data: ");
        System.out.println("name = foo");
        System.out.println("num = 100");
        System.out.println("balance = 1000.21");
        System.out.println("is_vip = true");
        System.out.println("Expected JSON string: {\"name\":\"foo\", \"num\":100, \"balance\":1000.21, \"is_vip\":true}");

        JSONObject obj = new JSONObject();
        obj.put("name","foo");
        obj.put("num",100);
        obj.put("balance",1000.21);
        obj.put("is_vip",true);
        StringWriter out = new StringWriter();
        try{
            obj.writeJSONString(out);
        }catch(Exception e){
            System.out.print("Error getting JSON string");
        }
        String jsonText = out.toString();
        System.out.print("Json String is: " + jsonText);
    }

    @Test public void testParseJSONstr(){
        System.out.println("Testing parse JSON str");
        String s = "{\"name\":\"foo\", \"num\":100, \"balance\":1000.21, \"is_vip\":true}";
        System.out.println("Parsing String: {\"name\":\"foo\", \"num\":100, \"balance\":1000.21, \"is_vip\":true}");
        JSONParser p = new JSONParser();
        try{
            JSONObject obj = (JSONObject)p.parse(s);
            StringWriter out = new StringWriter();
            try{
                obj.writeJSONString(out);
                String jsonText = out.toString();
                System.out.print("JSON object has the variables: " + jsonText);
            }catch(Exception e){
                System.out.print("Error getting JSON string");
            }
        }catch(ParseException pe){
            System.out.println("Problem parsing the JSON string");
        }
    }

    @Test public void testContainsSetting(){
        System.out.println("Testing getSetting method");
        StateController statectrl = new StateController();
        SettingsController settingsctrl = new SettingsController();
        statectrl.setSttingsController(settingsctrl);
        settingsctrl.setStateController(statectrl);

        //Change State to SETTINGS
        statectrl.changeState(PROGRAM_STATE.SETTINGS);

        boolean result;

        //create and set new settings in hashtable
        System.out.println("Creating Hashtable and Putting key values: isVerbose=true, dataTransferTechnique=a, and Transmitter=stub");
        Hashtable<String,String> newSettings = new Hashtable<String,String>();
        newSettings.put("isVerbose","true");
        newSettings.put("dataTransferTechnique","a");
        newSettings.put("transmitter","stub");
        System.out.println("Printing Hashtable keys and values before changing settings: ");
        System.out.println("isVerbose: " + newSettings.get("isVerbose"));
        System.out.println("dataTransferTechnique: " + newSettings.get("dataTransferTechnique"));
        System.out.println("transmitter: " + newSettings.get("transmitter"));

        //changing the settings
        System.out.println("Changing settings ...");
        result = settingsctrl.changeSettings(newSettings);
        if(result){
            System.out.println("Changed Settings Successfuly. Number of settings=" + settingsctrl.getNumberOfSettings());
        }else{
            System.out.println("Unable to change settings");
            return;
        }

        String settingName;

        //testing if SettingsController contains isVerbose
        settingName = "isVerbose";
        if(settingsctrl.containsSetting(settingName)){
            System.out.println("Setting " + settingName + " Found");
        }else{
            System.out.println("Setting " + settingName + " NOT Found!");
        }

        //testing if SettingsController contains dataTransferTechnique
        settingName = "dataTransferTechnique";
        if(settingsctrl.containsSetting(settingName)){
            System.out.println("Setting " + settingName + " Found");
        }else{
            System.out.println("Setting " + settingName + " NOT Found!");
        }

        //testing if SettingsController contains transmitter
        settingName = "transmitter";
        if(settingsctrl.containsSetting(settingName)){
            System.out.println("Setting " + settingName + " Found");
        }else{
            System.out.println("Setting " + settingName + " NOT Found!");
        }

        System.out.println("Test contains setting end");
    }

    @Test public void testGetSetting(){
        System.out.println("Testing getSetting method");
        StateController statectrl = new StateController();
        SettingsController settingsctrl = new SettingsController();
        statectrl.setSttingsController(settingsctrl);
        settingsctrl.setStateController(statectrl);

        //Change State to SETTINGS
        statectrl.changeState(PROGRAM_STATE.SETTINGS);

        String result;
        boolean isSuccess;

        //get setting when no setting in settings
        System.out.println("Testing getSetting with empty settings...");
        result = settingsctrl.getSetting("name");
        assertEquals(result,null);
        System.out.println("Test passed. value returned is: " + result);

        //create and set new settings in hashtable
        System.out.println("Putting settings isVerbose, dataTransferTechnique, and Transmitter");
        Hashtable<String,String> newSettings = new Hashtable<String,String>();
        newSettings.put("isVerbose","true");
        newSettings.put("dataTransferTechnique","a");
        newSettings.put("transmitter","stub");
        //System.out.println(newSettings);
        System.out.println("Printing settings before changing them: ");
        System.out.println("isVerbose: " + newSettings.get("isVerbose"));
        System.out.println("dataTransferTechnique: " + newSettings.get("dataTransferTechnique"));
        System.out.println("transmitter: " + newSettings.get("transmitter"));

        //Change settings
        isSuccess = settingsctrl.changeSettings(newSettings);
        assertEquals(isSuccess,true);
        System.out.println("Settings Changed Successfully. Number of settings: " + settingsctrl.getNumberOfSettings());

        //Start testing getSetting method
        System.out.println("Getting each of the settings: ");
        String settingName = "";

        //Get isVerbose
        settingName = "isVerbose";
        isSuccess = settingsctrl.containsSetting(settingName);
        if(isSuccess){
            result = settingsctrl.getSetting(settingName);
            System.out.println(settingName + " found. value=" + result);
        }else{
            System.out.println(settingName + " NOT Found!!!");
        }
        assertEquals(result,"true");

        //Get dataTransferTechnique
        settingName = "dataTransferTechnique";
        isSuccess = settingsctrl.containsSetting(settingName);
        if(isSuccess){
            result = settingsctrl.getSetting(settingName);
            System.out.println(settingName + " found. value=" + result);
        }else{
            System.out.println(settingName + " NOT Found!!!");
        }
        assertEquals(result,"a");

        //Get transmitter
        settingName = "transmitter";
        isSuccess = settingsctrl.containsSetting(settingName);
        if(isSuccess){
            result = settingsctrl.getSetting(settingName);
            System.out.println(settingName + " found. value=" + result);
        }else{
            System.out.println(settingName + " NOT Found!!!");
        }
        assertEquals(result,"stub");

        System.out.println("testing of getSetting finished");
    }

    @Test public void testChangeSetting(){
        System.out.println("Testing changeSetting method");
        StateController statectrl = new StateController();
        SettingsController settingsctrl = new SettingsController();
        statectrl.setSttingsController(settingsctrl);
        settingsctrl.setStateController(statectrl);

        //Change State to SETTINGS
        statectrl.changeState(PROGRAM_STATE.SETTINGS);

        String result;
        boolean isSuccess;

        //get setting when no setting in settings
        System.out.println("Testing getSetting with empty settings...");
        result = settingsctrl.getSetting("name");
        assertEquals(result,null);
        System.out.println("Test passed. value returned is: " + result);


        //Test change settings with JSON string with settings between quotation marks
        String settingStr = "{\"isVerbose\":\"true\", \"dataTransferTechnique\":\"a\", \"transmitter\":\"stub\"}";
        try{
            isSuccess = settingsctrl.changeSettings(settingStr);
            assertEquals(isSuccess,true);
            assertEquals(settingsctrl.getNumberOfSettings(),3);
            assertEquals(settingsctrl.getSetting("isVerbose"),"true");
            assertEquals(settingsctrl.getSetting("dataTransferTechnique"),"a");
            assertEquals(settingsctrl.getSetting("transmitter"),"stub");
            System.out.println("successfully changed settings");
        }catch(Exception e){
            System.out.println("Unable to change settings");
            e.printStackTrace();
        }
    }

    @Test public void testGetIsVerbose(){
        System.out.println("Testing changeSetting method");
        StateController statectrl = new StateController();
        SettingsController settingsctrl = new SettingsController();
        statectrl.setSttingsController(settingsctrl);
        settingsctrl.setStateController(statectrl);

        boolean isVerbose;
        //test with no isVerbose setting in the settings
        System.out.println("getting isVerbose when there are no settings and state is not SETTINGS");
        isVerbose = settingsctrl.getIsVerbose();
        assertEquals(isVerbose,false);
        System.out.println("isVerbose is false");

        //Change State to SETTINGS
        statectrl.changeState(PROGRAM_STATE.SETTINGS);

        //test isVerbose=true
        System.out.println("setting isVerbose=true");
        String settingStr = "{\"isVerbose\":\"true\"}";
        try{
            settingsctrl.changeSettings(settingStr);
            isVerbose = settingsctrl.getIsVerbose();
            assertEquals(isVerbose,true);
            System.out.println("getIsVerbose returned true");
        }catch(Exception e){
            System.out.println("Unable to change settings");
            e.printStackTrace();
        }

        //test isVerbose=false
        System.out.println("setting isVerbose=false");
        settingStr = "{\"isVerbose\":\"false\"}";
        try{
            settingsctrl.changeSettings(settingStr);
            isVerbose = settingsctrl.getIsVerbose();
            assertEquals(isVerbose,false);
            System.out.println("getIsVerbose returned true");
        }catch(Exception e){
            System.out.println("Unable to change settings");
            e.printStackTrace();
        }

    }
}
