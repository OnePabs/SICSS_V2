package server;

public class Timer implements Runnable {
    private long period;
    private Task periodic_task;
    boolean isVerbose;
    private boolean finish_execution;
    

    public Timer(long period, Task periodic_task, boolean isVerbose){
        this.period = period;
        this.periodic_task = periodic_task;
        this.finish_execution = false;
        this.isVerbose = isVerbose;
    }


    @Override
    public void run(){
        while(!this.finish_execution){
            Sleeper.sleep(this.period);
            if(!this.finish_execution){ //check again after sleep
                if(isVerbose){
                    System.out.println("Timer Starting Task");
                }
                Thread t = new Thread(this.periodic_task);
                t.start();
            }
        }

        if(isVerbose){
            System.out.println("Timer finished execution");
        }
    }

    public void finishExecution(){
        this.finish_execution = true;
    }
}