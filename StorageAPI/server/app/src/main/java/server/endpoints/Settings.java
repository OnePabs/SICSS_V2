package server.endpoints;

import java.io.IOException;
import java.util.Hashtable;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.inner_modules.SettingsController;

import java.io.IOException;
import java.io.InputStream;

public class Settings implements HttpHandler {
	private SettingsController settingsCtrl;
	
	public Settings(SettingsController settingsCtrl) {
		super();
		this.settingsCtrl = settingsCtrl;
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		if(settingsCtrl.getIsVerbose()){
			System.out.println("settings endpoint reached");
		}

		InputStream input_stream = t.getRequestBody();
		try {
			byte[] content = input_stream.readAllBytes();
			String jsonStr = String.valueOf(content);
			if(settingsCtrl.changeSettings(jsonStr)){
				t.sendResponseHeaders(200,-1);
			}
		}catch(Exception e) {
			if(settingsCtrl.getIsVerbose()){
				System.out.println("Exception happened at SETTINGS entry point");
			}
			e.printStackTrace();
			t.sendResponseHeaders(500, -1);
		}finally {
			input_stream.close();
		}
	}

}
