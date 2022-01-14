package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import StorageManager.MysqlApi;

public class InsertOne implements HttpHandler{

    private MysqlApi mysqlapi;

    public InsertOne(MysqlApi mysqlapi){
        this.mysqlapi = mysqlapi;
    }

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        boolean success = false;
        System.out.println("Storage Manager: InsertOne endpoint reached");
        Statement stmt = null;
        InputStream input_stream = t.getRequestBody();


        try{
            byte[] content = input_stream.readAllBytes();
            success = mysqlapi.insertone(content);
        }catch(Exception e){
            success = false;
            e.printStackTrace();
        }


        try{
            if(success){
                t.sendResponseHeaders(200,-1);
            }else{
                t.sendResponseHeaders(400,-1);
            }

        }catch (Exception ioe){
            System.out.println("Error: ");
            ioe.printStackTrace();
        }
    }
}