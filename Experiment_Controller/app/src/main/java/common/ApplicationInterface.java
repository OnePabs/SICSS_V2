package common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ApplicationInterface {
	public HttpClient client;
	public String address;
	
	public ApplicationInterface(String appAddress) {
		this.client = HttpClient.newBuilder().build();
		this.address = appAddress;
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
        }catch(Exception e){
			e.printStackTrace();
        	
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
        }catch(Exception e){
			e.printStackTrace();
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
        }catch(Exception e){
			e.printStackTrace();
		}
        return false;
	}
	
}
