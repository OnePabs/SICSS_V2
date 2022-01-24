package common;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.io.IOException;
import java.net.URI;

public class StorageManagerInterface {
    public HttpClient client;
    public String address;

    public StorageManagerInterface(String address) {
        this.client = HttpClient.newBuilder().build();
        this.address = address;
    }


    public boolean clear() {
        URI uri = URI.create(address + "/clear");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        try {
            HttpResponse<?>  response = client.send(request,BodyHandlers.discarding());
            if(response.statusCode() == 200) {
                return true;
            }
        }catch(IOException ioe){

        }catch(InterruptedException ie) {

        }
        return false;
    }

    public String getMeasurements() {
        URI uri = URI.create(address + "/measurements");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        try {
            HttpResponse<String>  response = client.send(request,BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                return response.body();
            }else {
                System.out.println("getMeasurements: Error code received: " + response.statusCode());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}