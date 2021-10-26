package server;

import server.endpoints.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import server.inner_modules.*;

public class Main {
	public static void main(String[] args) {
		int port = 8080;
		System.out.println("Storage API Version 2. Running on port: " + String.valueOf(8080));
		
		//create inner modules
		InnerModulesContainer ic = new InnerModulesContainer(port);
		ic.init();
		ic.start();
	}

}
