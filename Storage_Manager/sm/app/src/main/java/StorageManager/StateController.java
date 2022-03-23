package StorageManager;

import StorageManager.enumerators.PROGRAM_STATE;

public class StateController {
	private PROGRAM_STATE currentState;
	private SettingsController settingsController;
	
	
	public StateController() {
		//initialize state
		this.currentState = PROGRAM_STATE.INITIAL;
	}
	
	public synchronized void setSttingsController(SettingsController settingsController) {
		this.settingsController = settingsController;
	}
	
	
	public synchronized PROGRAM_STATE getCurrentState() {
		return currentState;
	}
	
	
	
	
	public synchronized boolean changeState(PROGRAM_STATE newState) {
		
		if(settingsController.getIsVerbose()) {
			System.out.print("State Controller received request to change state. State Requested: "+newState.toString()+". Current state: "+this.currentState.toString() + ". ");
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
			System.out.println("State Change allowed. ");
		}else if(settingsController.getIsVerbose() && !isChangeAllowed) {
			System.out.println("State Change not allowed. ");
		}
		return isChangeAllowed;
	}


	public synchronized boolean isStateRunning(){
		if(currentState == PROGRAM_STATE.RUNNING){
			return true;
		}
		return false;
	}
	
}
