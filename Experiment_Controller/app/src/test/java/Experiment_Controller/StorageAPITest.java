package Experiment_Controller;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class StorageAPITest {
	
	@Test public void testStart() {
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		assertEquals(sapi.start(),true);
	}
	
	@Test public void testStop() {
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		assertEquals(sapi.stop(),true);
	}

	@Test public void testSettingsEndpoint() {
		String jr = "{\"isVerbose\":\"true\",\"dataTransferTechnique\":\"a\"}";
		
		StorageAPIInterface sapi = new StorageAPIInterface("http://localhost:8080");
		if(sapi.changeSettings(jr)) {
			System.out.println("settings changed");
		}
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
}
