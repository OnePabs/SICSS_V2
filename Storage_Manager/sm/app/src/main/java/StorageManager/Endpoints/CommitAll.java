package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import StorageManager.MysqlApi;

public class CommitAll implements HttpHandler{

    private MysqlApi mysqlapi;

    public CommitAll(MysqlApi mysqlapi){
        this.mysqlapi = mysqlapi;
    }

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        boolean success = false;
        System.out.println("Storage Manager: commitall endpoint reached");
        InputStream input_stream = t.getRequestBody();

        try{
            byte[] content = input_stream.readAllBytes();
            JSONParser parser = new JSONParser();
            String jstr = new String(content,StandardCharsets.UTF_8);
            Object obj = parser.parse(jstr);
            JSONArray arr = (JSONArray)obj;
            success = mysqlapi.comitAll(arr);
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