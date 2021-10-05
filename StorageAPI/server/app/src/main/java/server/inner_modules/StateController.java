package server.inner_modules;

import server.enumerators.PROGRAM_STATE;

public class StateController {
	private PROGRAM_STATE currentState;
	private SettingsController settingsController;
	
	
	public StateController() {
		//initialize state
		this.currentState = PROGRAM_STATE.INITIAL;
	}
	
	synchronized public void setSttingsController(SettingsController settingsController) {
		this.settingsController = settingsController;
	}
	
	
	synchronized public PROGRAM_STATE getCurrentState() {
		return currentState;
	}
	
	
	
	
	synchronized public boolean changeState(PROGRAM_STATE newState) {
		
		if(settingsController.getIsVerbose()) {
			System.out.println("State Controller received request to change state. State Requested: "+newState.toString()+". Current state: "+this.currentState.toString());
		}
		
		
		boolean isChangeAllowed = false;
		if(this.currentState == newState) {
			if(settingsController.getIsVerbose()) {
				System.out.println("State Requested is same as current state: "+this.currentState.toString());
			}
			return true;
		}
		
		switch(this.currentState) {
		case INITIAL:
			if(newState == PROGRAM_STATE.SETTINGS) {
				this.currentState = PROGRAM_STATE.SETTINGS;
				isChangeAllowed = true;
			}
			break;
		case SETTINGS:
			if(newState == PROGRAM_STATE.RUNNING) {
				this.currentState = PROGRAM_STATE.RUNNING;
				isChangeAllowed = true;
			}
			break;
		case RUNNING:
			if(newState == PROGRAM_STATE.STOPPED) {
				this.currentState = PROGRAM_STATE.STOPPED;
				isChangeAllowed = true;
			}
			break;
		case STOPPED:
			if(newState == PROGRAM_STATE.RUNNING || newState == PROGRAM_STATE.SETTINGS) {
				this.currentState = newState;
				isChangeAllowed = true;
			}
			break;
		default:
			return false;
		}
		
		if(settingsController.getIsVerbose() && isChangeAllowed) {
			System.out.println("Change allowed. New current state: : " + this.currentState.toString());
		}else if(settingsController.getIsVerbose() && !isChangeAllowed) {
			System.out.println("Change not allowed. State is still: " + this.currentState.toString());
		}
		return isChangeAllowed;
	}
	
}
