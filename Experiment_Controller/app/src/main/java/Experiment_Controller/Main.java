package Experiment_Controller;

public class Main {

	public static void main(String[] args) {
		System.out.println("Experiment Controller");
		
		String jr = "{\"isVerbose\":\"true\",\"dataTransferTechnique\":\"a\"}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
	}

}
