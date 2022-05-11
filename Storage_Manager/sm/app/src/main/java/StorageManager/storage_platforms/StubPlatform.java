package StorageManager.storage_platforms;

import StorageManager.*;
import StorageManager.storage_platforms.*;
import StorageManager.service_time_creators.*;
import StorageManager.service_time_creators.creators.*;
import org.json.simple.JSONArray;

public class StubPlatform extends ParentStoragePlatform{
    private SettingsController settingsController;
    private ParentServiceTimeCreator parentServiceTimeCreator;

    public StubPlatform(SettingsController settingsController){
        this.settingsController = settingsController;
        parentServiceTimeCreator = ServiceTimeCreatorBuilder.build(settingsController);
    }

    public boolean insertOne(byte[] request){
        if(settingsController.getIsVerbose()){
            System.out.println("Stub Platform producing service time for insert one");
        }
        parentServiceTimeCreator.createServiceTime(request.length);
        return true;
    }
    public boolean commitAll(JSONArray arr){
        if(settingsController.getIsVerbose()){
            System.out.println("Stub Platform producing service time for commitAll");
        }
        int num_req=arr.size();
        parentServiceTimeCreator.createServiceTime(num_req);
        return true;
    }
}