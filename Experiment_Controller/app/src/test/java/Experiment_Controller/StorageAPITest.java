package Experiment_Controller;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class StorageAPITest {
	
	@Test public void changeSettings() {
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		String jr = "{\"isVerbose\":\"true\",\"dataTransferTechnique\":\"a\"}";
		System.out.println("Changing settings to: " + jr);
		assertEquals(sapi.changeSettings(jr),true);
		System.out.println("Successfully changed Storage API settings");
	}
	
	@Test public void testStates() {
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		String jr = "{\"isVerbose\":\"true\",\"dataTransferTechnique\":\"a\"}";
		
		//initial state unnavailable states
		assertEquals(sapi.start(),false);
		assertEquals(sapi.stop(),false);
		
		//Go to settings
		assertEquals(sapi.changeSettings(jr),true);
		
		//Settings unavailable states
		assertEquals(sapi.stop(),false);
		
		//Go to start
		assertEquals(sapi.start(),true);
		
		//Start unavailable states
		assertEquals(sapi.changeSettings(jr),false);
		
		//go to stop
		assertEquals(sapi.stop(),true);
		
		//go to start from stop
		assertEquals(sapi.start(),true);
		
		//go back to stop
		assertEquals(sapi.stop(),true);
		
		//go to settings from stop
		assertEquals(sapi.changeSettings(jr),true);
		
		System.out.println("All state transitions tested");
	}
	
	@Test public void testDataEndpoint() {
		System.out.println("Testing sending data to Storage API using Storage API Experiment Controller Interface");
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		String jr = "{\"isVerbose\":\"true\",\"dataTransferTechnique\":\"a\"}";
		
		//change settings
		assertEquals(sapi.changeSettings(jr),true);
		
		//start
		assertEquals(sapi.start(),true);
		
		//send data
		assertEquals(sapi.sendData("hello world!"),true);
		System.out.println("Test completed successfully");
	}
}
