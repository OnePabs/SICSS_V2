package StorageManager.storage_platforms;

public class ParentStoragePlatform {
    public boolean insertOne(byte[] request){
        System.out.println("Parent storage platform: insert one");
        return true;
    }
    public boolean commitAll(String content){
        System.out.println("Parent storage platform: comit all");
        return true;
    }
}