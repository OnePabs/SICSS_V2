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
    public final int MAX_NUM_TRIES = 3;

    public StorageManagerInterface(String address) {
        this.client = HttpClient.newBuilder().build();
        this.address = address;
    }

    public boolean start() {
		URI uri = URI.create(address + "/start");
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
	
	public boolean stop() {
		URI uri = URI.create(address + "/stop");
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

    public boolean changeSettings(String jsonStr) {

        URI uri = URI.create(address + "/settings");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(BodyPublishers.ofString(jsonStr))
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
        for(int i=0; i<MAX_NUM_TRIES;i++){
            try{
                HttpResponse<String>  response = client.send(request,BodyHandlers.ofString());
                if(response.statusCode() == 200) {
                    return response.body();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            System.out.println("Unnable to get Manager measurements. Retrying...");
            try{
                Thread.sleep(1000);
            }catch(Exception e){}
        }
        System.out.println("Final - Unnable to get measurements from Storage Manager");
        return "";
    }
}
