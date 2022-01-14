package StorageManager;

public class Parser {
    public static int getRequestId(byte[] content){
        //uses big endian to parse first two bytes to get the request Id
        int most_significant = content[0];
        //System.out.println("first byte: " + most_significant);
        int least_significant = content[1];
        //System.out.println("second byte: " + least_significant);
        int requestId = most_significant*16*16 + least_significant;
        //System.out.println("requestID: " + requestId);
        return requestId;
    }

    public static int getAppId(byte[] content){
        return content[2];
    }

    public static int getBatchId(byte[] content){
        return content[3];
    }
}