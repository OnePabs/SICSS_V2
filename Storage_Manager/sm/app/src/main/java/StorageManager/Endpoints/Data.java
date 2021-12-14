package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class Data implements HttpHandler{
    private Connection sqlcon;
    public Data(Connection sqlcon){
        this.sqlcon = sqlcon;
    }

    @Override
    public void handle(HttpExchange t) {
        //connect to database
        //save data into table

        System.out.println("Storage Manager: data endpoint reached");
        Statement stmt = null;
        InputStream input_stream = t.getRequestBody();


        try{
            byte[] content = input_stream.readAllBytes();

            stmt = sqlcon.createStatement();

            String query1 = "INSERT INTO data " + "VALUES (" + content[0] + "," + content[1] + "," + content[2] + "," + content[3] + "," + content + ")";
            stmt.executeUpdate(query1);
        }catch(SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }catch(Exception e){
            e.printStackTrace();
        }


        try{
            t.sendResponseHeaders(200,-1);
        }catch (Exception ioe){
            System.out.println("data: Error: ");
            ioe.printStackTrace();
        }
    }
}
