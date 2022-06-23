package server.inner_modules.data_transfer_technique.techniques;

import server.Task;

public class PeriodTask extends Task {
    private long period;
    private boolean isFinished;
    private Object notifier;

    public PeriodTask(long period, Object notifier){
        this.period = period;
        this.notifier = notifier;
    }

    public void finishExecution(){
        this.isFinished = true;
    }

    @Override
    public void run(){
        if(!this.isFinished){
            //sleep for the period specified
            //stop sleeping if this thread receives the command to stop execution
            long start_time = System.currentTimeMillis();
            long duration = 0;
            while(duration < period && !this.isFinished){
                try{
                    Thread.sleep(0,500000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    duration = System.currentTimeMillis() - start_time;
                }
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

}