//keeps the state of the application
package StorageManager;

import java.sql.*;

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
        int requestId = 0;
        int appId = 0;
        int batchId = 0;
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

}