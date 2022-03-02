package server.inner_modules.data_transfer_technique;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.enumerators.PROGRAM_STATE;
import server.enumerators.TIMESTAMP_NAME;
import server.inner_modules.*;

public class ParentDataTransferTechnique implements Runnable{
    protected String techniqueName;
    protected StateController stateController;
    protected SettingsController settingsController;
    protected ReadyLists buffer;
    protected TransmitionInformationObject transmitionInformationObject;
    private boolean isRunning;

    //constructor
    public ParentDataTransferTechnique(
        StateController stateController, 
        SettingsController settingsController,
        ReadyLists buffer,
        TransmitionInformationObject transmitionInformationObject
        ){
            this.stateController = stateController;
            this.settingsController = settingsController;
            this.buffer = buffer;
            this.transmitionInformationObject = transmitionInformationObject;
            this.isRunning = true;
        }


    //GETTERS
    public String getTechniqueName(){
        return this.techniqueName;
    }


    //Helper Methods
    public void waitForNewBufferEntry() throws Exception{
        synchronized(buffer.bufferEntryEvent){
            buffer.bufferEntryEvent.wait();
        }
        if(settingsController.getIsVerbose()){
            System.out.println("Number of Requests in the Buffer: " + buffer.getNumberOfRequestsInBuffer());
             System.out.println("Number of Bytes in the Buffer: " + buffer.getNumberOfBytesInBuffer());
        }
    }

    //METHODS to be Overridden by child
    public boolean initialize(){return true;}
    public void waitForDataTransferCondition() throws Exception{
        waitForNewBufferEntry();
    }
    public void transmit() throws Exception{
        synchronized(transmitionInformationObject.transmitionNotifier){
            transmitionInformationObject.transmitionNotifier.notifyAll();
        }
        if(settingsController.getIsVerbose()){
            System.out.println("Data transfer technique notified transmitter to transmit");
        }
    }


    //RUN METHOD. DO NOT Override
    @Override
    public void run() {
    	if(settingsController.getIsVerbose()){
    		System.out.println("Data transfer " + this.techniqueName + " started");
    	}

    	if(initialize()){
            while(isRunning){
                try{
                    if(stateController.isStateRunning() && isRunning){
                        waitForDataTransferCondition();
                    }
                    
                    //check condition again after waiting
                    if(stateController.isStateRunning() && isRunning){
                        //tell transmitter to transmit specific requests depending on the technique
                        transmit();
                    }else if(isRunning){
                        //State is NOT running but thread is not finished execution
                        Thread.sleep(100);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            if(settingsController.getIsVerbose()){
                System.out.println("Transfer Technique: Finishing execution");
            }
        }else{
            if(settingsController.getIsVerbose()){
                System.out.println("Data transfer technique unable to initialize...");
            }
        }
    }

    public void finishExecution(){
        //ends the execution of the data transfer thread
        this.isRunning = false;
    }

}
