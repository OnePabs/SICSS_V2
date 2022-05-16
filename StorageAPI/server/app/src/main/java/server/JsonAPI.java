package server;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.Set;
import java.util.Hashtable;

public class JsonAPI {
    public static Hashtable<String,Object> jsonToHashTable(JSONObject jsonObject){

        try {
            //Copy JSON settings into HashTable settings
            Hashtable<String, Object> ht = new Hashtable<String, Object>();
            Set<Object> keys = jsonObject.keySet();
            for (Object key : keys) {
                ht.put(String.valueOf(key), jsonObject.get(key));
            }
            return ht;
        }catch(Exception e) {
            System.out.println("Error parsing JSONObject into hashtable. ");
            System.out.println(e);
            return null;
        }
    }


    public static Hashtable<String,Object> jsonToHashTable(String jsonStr){
        JSONParser parser = new JSONParser();
        try {
            //parse JSON string into JSON object {key:value,...}
            JSONObject jasonObject = (JSONObject)parser.parse(jsonStr);

            return jsonToHashTable(jasonObject);
        }catch(ParseException pe) {
            System.out.println("Error parsing String into JSONObject. ");
            System.out.println(pe);
            return null;
        }
    }

    public static Hashtable<String,Object> jsonToHashTable(Object obj){
        try {
            JSONObject jsonObject = (JSONObject) obj;
            return jsonToHashTable(jsonObject);
        }catch(Exception e){
            System.out.println("Error converting object to JSONObject.");
            e.printStackTrace();
            return null;
        }
    }

    public static Long jsonObjectToLong(JSONObject jsonObject){
        Long l = Long.valueOf(0);
        //String jsonS = jsonObject.
        return l;
    }

    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }


}
