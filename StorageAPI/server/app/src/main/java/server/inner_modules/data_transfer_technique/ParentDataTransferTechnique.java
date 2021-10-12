package server.inner_modules.data_transfer_technique;

import server.data_structures.IORequest;
import server.data_structures.SyncIORequestLinkedList;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.service_time_creators.ParentServiceTimeCreator;

public class ParentDataTransferTechnique implements Runnable{
    protected String techniqueName;
    protected SettingsController settingsController;
    protected StateController stateController;
    protected ParentServiceTimeCreator parentServiceTimeCreator;

    //variables for data transfer technique
    protected SyncIORequestLinkedList ioEntryList;
    private boolean isExecutionSupposedToFinish;

    //constructor
    public ParentDataTransferTechnique(){}

    //SETTERS
    public void setStateController(StateController stateController){
        this.stateController = stateController;
    }
    public void setSettingsController(SettingsController settingsController){
        this.settingsController = settingsController;
    }
    public void setIoEntryList(SyncIORequestLinkedList ioEntryList){
        this.ioEntryList = ioEntryList;
    }
    public void setParentServiceTimeCreator(ParentServiceTimeCreator parentServiceTimeCreator) {
        this.parentServiceTimeCreator = parentServiceTimeCreator;
    }


    //GETTERS
    public String getTechniqueName(){
        return this.techniqueName;
    }

    //METHODS used within RUN  (these must be @Override by child classes
    public boolean isTransferConditionSatisfied(){return true;} //condition for sending ready IO requests
    public void handle(){}
    public void transmit(){}
    public void clear(){}


    //RUN METHOD. DO NOT Override
    @Override
    public void run() {
        while(!isExecutionSupposedToFinish){
            if(stateController.getCurrentState() == PROGRAM_STATE.RUNNING){
                //data transfer technique is allowed to run
                if(isTransferConditionSatisfied()){
                    //transmit data
                    try{
                        transmit();
                    }catch(Exception e){
                        if(settingsController.getIsVerbose()){
                            e.printStackTrace();
                        }
                    }
                }else{
                    //process and handle next IO request from ioEntryList
                    parentServiceTimeCreator.createServiceTime();
                    handle();
                }
            }else{
                //State is NOT running. Transfer technique is not allowed to run
                try{
                    Thread.sleep(100);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        clear();
        if(settingsController.getIsVerbose()){
            System.out.println("Data Transfer Technique finished executing. Thread ends");
        }
    }

    public void finishExecution(){
        //ends the execution of the data transfer thread
        isExecutionSupposedToFinish = true;
    }

}
