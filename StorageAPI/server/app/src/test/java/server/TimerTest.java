package server;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import server.Sleeper;
import server.Timer;
import server.Task;

public class TimerTest {
    @Test public void testTimer(){
        long period = 1000;
        
        StubTask task = new StubTask();
        Timer ti = new Timer(period,task, true);
        
        Thread tr = new Thread(ti);
        tr.start();
        

        Sleeper.sleep(period*5);

        //stop timer
        ti.finishExecution();

        Sleeper.sleep(1000);

        System.out.println("Parent Thread finished");
    }
}

class StubTask extends Task{

    @Override
    public void run(){
        long current_time = System.currentTimeMillis();
        System.out.println(current_time);
    }
}