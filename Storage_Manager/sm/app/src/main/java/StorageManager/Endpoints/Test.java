package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Test implements HttpHandler{

    @Override
    public void handle(HttpExchange t) {
        //insert one row to db table content
        boolean success = true;
        System.out.println("Storage Manager: Test endpoint reached");

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