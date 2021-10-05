package server;

import server.endpoints.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import server.inner_modules.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Storage API Version 2");
		
		//create inner modules
		StateController stateCtrl = new StateController();
		SettingsController settingsCtrl = new SettingsController(stateCtrl);
		stateCtrl.setSttingsController(settingsCtrl);
		
		//Set up endpoints
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/settings", new Settings(settingsCtrl));
			server.setExecutor(null);
			server.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
