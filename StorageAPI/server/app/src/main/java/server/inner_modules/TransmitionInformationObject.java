package server.inner_modules;

public class TransmitionInformationObject {
    private boolean isTranmsitionInProgress;

    public TransmitionInformationObject(){
        isTranmsitionInProgress = false;
    }

    public synchronized void waitForNotificationToStartTransmition() throws Exception{
        if(!isTranmsitionInProgress){
            wait();
            isTranmsitionInProgress = true; //set isTranmsitionInProgress to true so threads to not notify to start other transmition
        }else{
            throw new Exception("ITO: wait called when transmition was already taking place. 1 thread cannot wait and perform transmition at the same time");
        }


    }

    public synchronized boolean sendNotificationToStartTransmition() throws Exception{
        if(isTranmsitionInProgress){
            //a transmition is already in progress. Cannot notify to start another one
            return false;
        }else{
            notifyAll();
            return true;
        }
    }


    public synchronized void endOfTransmition(){
        isTranmsitionInProgress = false;
    }
}