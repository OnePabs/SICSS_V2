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
		InnerModulesContainer ic = new InnerModulesContainer(8080);
		ic.init();
		ic.start();
	}

}
