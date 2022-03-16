package server;

public class Sleeper {
    public static void sleep(long period){
        long start_time = System.currentTimeMillis();
        long duration = 0;
        while(duration < period){
            try{
                Thread.sleep(0,500000);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                duration = System.currentTimeMillis() - start_time;
            }
        }
    }
}