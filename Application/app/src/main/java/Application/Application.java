package Application;

import distribution_generators.ParentNaturalNumberGenerator;
import enumerators.PROGRAM_STATE;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Application implements Runnable{
    private ParentNaturalNumberGenerator naturalNumberGenerator;
    private StateController stateController;
    private SettingsController settingsController;
    private String receiverAddress;
    private boolean isFinished;
    private boolean isVerbose;
    private byte[] content;


    public Application(
            StateController stateController,
            SettingsController settingsController,
            ParentNaturalNumberGenerator naturalNumberGenerator,
            String receiverAddress,
            boolean isVerbose
            ){
        this.stateController= stateController;
        this.settingsController = settingsController;
        this.naturalNumberGenerator = naturalNumberGenerator;
        this.receiverAddress = receiverAddress;
        this.content = new byte[100];
        content[0] = 0;
        content[1] = 0;
        content[2] = 1;
        content[3] = 1;
        for(int i=4;i<100;i++){
            content[i] = 1;
        }
        this.isFinished = false;
        this.isVerbose = isVerbose;
    }

    @Override
    public void run(){
        //set up
        int requestNum = 0;
        int leastSignificant;
        int mostSignificant;
        naturalNumberGenerator.initialize();
        HttpClient client = HttpClient.newBuilder().build();
        if(isVerbose){
            System.out.println("Receiver address: " + receiverAddress);
        }
        URI uri = URI.create(receiverAddress);
        HttpRequest request;

        long sleepTime;

        boolean isFirstCycle = true;
        long cycle_start_time = 0;
        int cycle_num = 0;
        int max_num_cycles = 2;
        long maxTime = (long)settingsController.getSetting("cycletime");

        while(!isFinished && stateController.getCurrentState()!= PROGRAM_STATE.FINISHED){
            if(stateController.getCurrentState()==PROGRAM_STATE.RUNNING){
                try {
                    if(settingsController.containsSetting("useMultipleRates") && settingsController.getBoolean("useMultipleRates")){
                        //Rate is a cycle
                        if(isFirstCycle){
                            cycle_start_time = System.currentTimeMillis();
                            isFirstCycle = false;
                        }

                        sleepTime = naturalNumberGenerator.generate()*(cycle_num%max_num_cycles + 1);

                        if((System.currentTimeMillis() - cycle_start_time) >= maxTime){
                            //generate request
                            cycle_num++;
                            cycle_start_time = System.currentTimeMillis();
                        }
                    }else{
                        sleepTime = naturalNumberGenerator.generate();
                    }

                    //Send request
                    long start_time = System.currentTimeMillis();
                    if(isVerbose){
                    	System.out.println("Application: sleeping for: " + String.valueOf(sleepTime) + " milliseconds");
                    }
                    
                    if(sleepTime != 0){
                        if(settingsController.getBoolean("useSleepForMockProcessing")){
                            Thread.sleep(sleepTime);
                        }else{
                            processFor(sleepTime);
                        }
                    }

                    leastSignificant = requestNum % 255;
                    mostSignificant = (requestNum-leastSignificant)/256;
                    content[0] = (byte)(((byte)mostSignificant) & 0xFF);
                    content[1] = (byte)(((byte)leastSignificant) & 0xFF);
                    requestNum++;
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(content))
                            .build();
                    if(isVerbose){
                    	System.out.println("Application: sending data to receiver...");
                    }
                    
                    HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
                    if(response.statusCode() != 200 && settingsController.getIsVerbose()==true) {
                        System.out.println("Application: Error Code other than 200 was received when sending data");
                    }else if(isVerbose){
                        long duration = System.currentTimeMillis() - start_time;
                        System.out.println("Application: data send successfully with ia="+duration+" milliseconds");
                    }

                }catch(Exception e){
                    if(settingsController.getIsVerbose()==true) {
                        System.out.println("Application: Exception thrown when sending data");
                        e.printStackTrace();
                    }
                }
            }else{
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finishing application...");
    }


    public void finishApplication(){
        isFinished = true;
    }


    public void processFor(long sleepTime){
        long startTime = System.currentTimeMillis();
        sleepTime = sleepTime - 1;
        do{
            /*
            int number = 1000;
            int currFactor = 0;
            for(int i=1;i<number;i++){
                if(number%i == 0){
                    currFactor = i;
                }
            }
            //long p = System.nanoTime() - startTime;
            //System.out.println("Time for one iteration of " +number+ " in nano: " + p);
            //System.exit(0);
            */
            try{
                Thread.sleep(0,300000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }while((System.currentTimeMillis()-startTime)<sleepTime);
    }
}
