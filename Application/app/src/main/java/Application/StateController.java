package Application;

import enumerators.PROGRAM_STATE;

public class StateController {
    private PROGRAM_STATE currentState;

    public StateController(){
        this.currentState = PROGRAM_STATE.INITIAL;
    }

    public PROGRAM_STATE getCurrentState(){
        return this.currentState;
    }

    public boolean changeState(PROGRAM_STATE newState){
        if(currentState == newState){
            return true;
        }

        boolean isChangeAllowed;
        switch(currentState){
            case INITIAL:
                if(newState == PROGRAM_STATE.SETTINGS){
                    isChangeAllowed = true;
                }else{
                    isChangeAllowed = false;
                }
                break;
            case SETTINGS:
                if(newState == PROGRAM_STATE.RUNNING){
                    isChangeAllowed = true;
                }else{
                    isChangeAllowed = false;
                }
                break;
            case RUNNING:
                if(newState == PROGRAM_STATE.STOPPED){
                    isChangeAllowed = true;
                }else{
                    isChangeAllowed = false;
                }
                break;
            case STOPPED:
                if(newState == PROGRAM_STATE.SETTINGS ||
                   newState == PROGRAM_STATE.RUNNING ||
                   newState == PROGRAM_STATE.FINISHED
                ){
                    isChangeAllowed = true;
                }else{
                    isChangeAllowed = false;
                }
                break;
            default:
                return false;
        }
        if(isChangeAllowed){
            currentState = newState;
        }
        return isChangeAllowed;
    }
}
