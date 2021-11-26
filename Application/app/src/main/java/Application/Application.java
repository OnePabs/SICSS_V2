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
        content[0] = 1;
        content[1] = 1;
        content[2] = 1;
        content[3] = 1;
        this.isFinished = false;
        this.isVerbose = isVerbose;
    }

    @Override
    public void run(){
        //set up
        naturalNumberGenerator.initialize();
        HttpClient client = HttpClient.newBuilder().build();
        URI uri = URI.create(receiverAddress + "/data");
        HttpRequest request;
        while(!isFinished && stateController.getCurrentState()!= PROGRAM_STATE.FINISHED){
            if(stateController.getCurrentState()==PROGRAM_STATE.RUNNING){
                try {
                    long sleepTime = naturalNumberGenerator.generate();
                    if(isVerbose){
                    	System.out.println("Application: sleeping for: " + String.valueOf(sleepTime) + " milliseconds");
                    }
                    
                    if(sleepTime == 0)return;
                    Thread.sleep(sleepTime);

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
                        System.out.println("Application: data send successfully ");
                    }

                }catch(Exception e){
                    if(settingsController.getIsVerbose()==true) {
                        System.out.println("Application: Exception thrown when sending data");
                        e.printStackTrace();
                        return;
                    }
                }
            }else{
                try{
                    Thread.sleep(300);
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
}
