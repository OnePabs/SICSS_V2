package common;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.io.IOException;
import java.net.URI;

public class StorageAPIInterface {
	public HttpClient client;
	public String address;
	
	public StorageAPIInterface(String storageApiAddress) {
		this.client = HttpClient.newBuilder().build();
		this.address = storageApiAddress;
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
	
	public boolean sendData(String data) {
		URI uri = URI.create(address + "/data");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(BodyPublishers.ofString(data))
                .build();
        try {
        	HttpResponse<?>  response = client.send(request,BodyHandlers.discarding());
        	if(response.statusCode() == 200) {
        		return true;
        	}else {
        		System.out.println("Error code received: " + response.statusCode());
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
		try {
        	HttpResponse<String>  response = client.send(request,BodyHandlers.ofString());
        	if(response.statusCode() == 200) {
        		return response.body();
        	}else {
        		System.out.println("getMeasurements: Error code received: " + response.statusCode());
        	}
        }catch(IOException ioe){
        	
        }catch(InterruptedException ie) {
        	
        }
        return "";
	}
}
