//keeps the state of the application
package StorageManager;

import java.sql.*;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MysqlApi {

    //db connection info
    private String host = "127.0.0.1";
    private String user = "root";
    private String password = "milo";
    private String dbname = "STORAGEDB";
    private String dburl = "jdbc:mysql://" + host + "/"+dbname;
    private String query = "INSERT INTO content (requestId, appId, batchId, content) VALUES (?,?,?,?)";

    public MysqlApi(){}

    public synchronized boolean insertone(byte[] content){
        if(content==null || content.length < 4){
            return false;
        }
        int requestId = Parser.getRequestId(content);
        int appId = Parser.getAppId(content);
        int batchId = Parser.getBatchId(content);
        try {
            Connection con = DriverManager.getConnection(dburl, user, password);
            con.setAutoCommit(false);
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, requestId);
            preparedStmt.setInt(2, appId);
            preparedStmt.setInt(3, batchId);
            preparedStmt.setString(4, content.toString());
            preparedStmt.execute();
            con.commit();

            preparedStmt.close();
            con.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public synchronized boolean comitAll(JSONArray arr){
        try{
            Connection con = DriverManager.getConnection(dburl, user, password);
            con.setAutoCommit(false);
            PreparedStatement preparedStmt = con.prepareStatement(query);

            for (int i=0; i < arr.size(); i++) {
                JSONObject jobj = (JSONObject)arr.get(i);
                preparedStmt.setInt(1, ((Long)jobj.get("requestId")).intValue());
                preparedStmt.setInt(2, ((Long)jobj.get("appId")).intValue());
                preparedStmt.setInt(3, ((Long)jobj.get("batchId")).intValue());
                preparedStmt.setString(4, jobj.get("content").toString());
                preparedStmt.addBatch();
            }

            preparedStmt.executeBatch();
            con.commit();
            preparedStmt.close();
            con.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


}

