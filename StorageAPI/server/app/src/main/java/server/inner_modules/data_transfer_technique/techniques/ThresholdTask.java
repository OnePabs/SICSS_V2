package server.inner_modules.data_transfer_technique.techniques;

import server.data_structures.*;
import server.Task;

public class ThresholdTask extends Task {
    private long threshold;
    private boolean isFinished;
    private Object notifier;
    private ReadyLists buffer;

    public ThresholdTask(ReadyLists buffer, long threshold, Object notifier){
        this.buffer = buffer;
        this.threshold = threshold;
        this.notifier = notifier;
    }

    public void finishExecution(){
        this.isFinished = true;
    }


    @Override
    public void run() {
        int numBytes = buffer.getNumberOfBytesInBuffer();
        while(numBytes < threshold && !this.isFinished){
            //Poll buffer for number of bytes
            try{
                Thread.sleep(0,500000);
            }catch(Exception e){
                e.printStackTrace();
            }
            //update number of bytes in the buffer
            numBytes = buffer.getNumberOfBytesInBuffer();
        }

        if(!this.isFinished){
            synchronized(notifier){
                if(!this.isFinished){
                    notifier.notifyAll();
                }
            }
        }
    }
}