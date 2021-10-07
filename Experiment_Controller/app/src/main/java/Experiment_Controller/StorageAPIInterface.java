package Experiment_Controller;

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
}
