package StorageManager.Endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Data implements HttpHandler{

    @Override
    public void handle(HttpExchange t) {
        //connect to database
        //save data into table

        System.out.println("Storage Manager: data endpoint reached");

        try{
            t.sendResponseHeaders(200,-1);
        }catch (Exception ioe){
            System.out.println("data: Error: ");
            ioe.printStackTrace();
        }
    }
}
