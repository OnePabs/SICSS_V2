package server.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;



public class Settings implements HttpHandler {
	private SettingsController settingsCtrl;
	private StateController stateController;
	
	public Settings(StateController stateController, SettingsController settingsCtrl) {
		super();
		this.settingsCtrl = settingsCtrl;
		this.stateController = stateController;
	}
	
	@Override
	public void handle(HttpExchange t) {
		if(settingsCtrl.getIsVerbose()){
			System.out.println("settings endpoint reached. isVerbose=true");
		}
		
		
		InputStream input_stream = t.getRequestBody();
		int returnCode;

		try {
			byte[] content = input_stream.readAllBytes();
			String jsonStr = new String(content, StandardCharsets.UTF_8);

			if(settingsCtrl.changeSettings(jsonStr)){
				returnCode = 200;
				if(settingsCtrl.getIsVerbose()){
					System.out.println("Settings EndPoint: Successfully changed settings to: "+jsonStr);
				}
			}else{
				returnCode = 400;
			}
			t.sendResponseHeaders(returnCode,-1);
		}catch(Exception e) {
			if(settingsCtrl.getIsVerbose()){
				System.out.println("Exception happened at SETTINGS entry point");
				e.printStackTrace();
			}

			try{
				t.sendResponseHeaders(400, -1);
				input_stream.close();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}

}
