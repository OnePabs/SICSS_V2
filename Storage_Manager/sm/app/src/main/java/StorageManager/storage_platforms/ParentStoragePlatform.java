package StorageManager.storage_platforms;

import org.json.simple.JSONArray;
public class ParentStoragePlatform {
    public boolean insertOne(byte[] request){
        System.out.println("Parent storage platform: insert one");
        return true;
    }
    public boolean commitAll(JSONArray arr){
        System.out.println("Parent storage platform: comit all");
        return true;
    }
}