package server.inner_modules.data_transfer_technique;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.enumerators.PROGRAM_STATE;
import server.enumerators.TIMESTAMP_NAME;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.service_time_creators.ParentServiceTimeCreator;
import server.inner_modules.transmitters.ParentTransmitter;

public class ParentDataTransferTechnique implements Runnable{
    protected String techniqueName;
    protected SettingsController settingsController;
    protected StateController stateController;
    protected ParentServiceTimeCreator parentServiceTimeCreator;
    protected ReadyLists readyLists;
    protected ParentTransmitter transmitter;

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
    public void setTransmitter(ParentTransmitter transmitter){
        this.transmitter = transmitter;
    }
    public void setReadyLists(ReadyLists readyLists){this.readyLists=readyLists;}
    //GETTERS
    public String getTechniqueName(){
        return this.techniqueName;
    }


    //METHODS that MUST be Overridden by child
    public boolean isTransferConditionSatisfied(){return true;} //condition for sending ready IO requests
    public void transmit(){}



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
                    //take request from Entry List and put it in ready lists
                    try{
                        //Take a request from the Entry List
                        IORequest request = this.ioEntryList.take();
                        //add timestamp to request
                        request.addTimeStamp(TIMESTAMP_NAME.ENTRY_LIST_EXIT);

                        //process and handle next IO request from ioEntryList
                        request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_START);
                        parentServiceTimeCreator.createServiceTime();
                        request.addTimeStamp(TIMESTAMP_NAME.SERVICE_TIME_END);

                        //add request to ready lists
                        request.addTimeStamp(TIMESTAMP_NAME.READY_LIST_ENTRY);
                        readyLists.add(request);
                        if(settingsController.getIsVerbose()){
                            StringBuilder sb = new StringBuilder();
                            for (byte b : request.getContent()) {
                                sb.append(String.format("%02X ", b));
                            }
                            System.out.println("Took IO Request from IOEntryList and added it to readylists. Body:");
                            System.out.println("IORequest Body taken by data transfer technique from readylist: "+ sb.toString());
                        }
                    }catch (Exception e){
                        if(settingsController.getIsVerbose()) {
                            e.printStackTrace();
                        }
                    }
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
        readyLists.clear();
        if(settingsController.getIsVerbose()){
            System.out.println("Data Transfer Technique finished executing. Thread ends");
        }
    }

    public void finishExecution(){
        //ends the execution of the data transfer thread
        isExecutionSupposedToFinish = true;
    }

}
