package server.inner_modules.transmitters.transmitter_types;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import server.data_structures.IORequest;
import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.data_structures.TimeStamp;
import server.enumerators.PROGRAM_STATE;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.transmitters.ParentTransmitter;

import static server.enumerators.TIMESTAMP_NAME.TRANSMITTER_ENTRY;
import static server.enumerators.TIMESTAMP_NAME.TRANSMITTER_EXIT;

public class StorageManagerTransmitter extends ParentTransmitter {
    private HttpClient client;
    private URI uri_insertone;
    private URI uri_comitall;
    private HttpRequest httpRequest;


    public StorageManagerTransmitter(
            StateController stateController,
            SettingsController settingsController,
            ReadyLists readyLists,
            MeasurementController measurementController
    ){
        super(stateController,settingsController,readyLists,measurementController);
        String uriStr = settingsController.getSetting("destination").toString();;
        client = HttpClient.newBuilder().build();
        uri_insertone = URI.create(uriStr + "/insertone");
        uri_comitall = URI.create(uriStr + "/commitall");
    }

    @Override
    public void transmit(IORequest request){
        if(stateController.getCurrentState()== PROGRAM_STATE.RUNNING){
            request.addTimeStamp(TRANSMITTER_ENTRY);
            for(TimeStamp t: request.getTimeStamps()){
                measurementController.addMeasurement(t);
            }
            request.addTimeStamp(TRANSMITTER_EXIT);
            if(settingsController.getIsVerbose()){
                System.out.println("Storage Manager Transmitter: IORequest " + request.getRequestId() + " leaving storage  API");
            }

            //send request
            try{
                httpRequest = HttpRequest.newBuilder()
                        .uri(uri_insertone)
                        .POST(HttpRequest.BodyPublishers.ofByteArray(request.getContent()))
                        .build();
                HttpResponse<?> response = client.send(httpRequest, HttpResponse.BodyHandlers.discarding());
                if(response.statusCode() != 200 && settingsController.getIsVerbose()==true) {
                    System.out.println("strg manager transmitter: Error Code other than 200 was received when sending data");
                }else if(settingsController.getIsVerbose()){
                    System.out.println("strg manager transmitter: data send successfully ");
                }
            }catch(Exception e){
                if(settingsController.getIsVerbose()==true) {
                    System.out.println("strg manager transmitter: Exception thrown when sending data");
                    e.printStackTrace();
                    return;
                }
            }

            measurementController.addMeasurement(request.getTimeStamp(TRANSMITTER_EXIT));
        }
    }

    @Override
    public void transmit(SyncIORequestLinkedList requests){
        if(stateController.getCurrentState()== PROGRAM_STATE.RUNNING){
            if(settingsController.getIsVerbose()){
                System.out.println("Storage Manager Transmitter: commitall");
            }

            try{
                IORequest request;
                String jsonstr = "[";
                int numRequests = requests.getSize();
                for(int i=0;i<numRequests;i++){
                    if(i != 0){
                        jsonstr += ",";
                    }
                    request = requests.take();
                    jsonstr += "{";
                    jsonstr += "\"requestId\":";
                    jsonstr += request.getRequestId();
                    jsonstr += ",";
                    jsonstr += "\"appId\":";
                    jsonstr += request.getAppId();
                    jsonstr += ",";
                    jsonstr += "\"batchId\":";
                    jsonstr += request.getBatchId();
                    jsonstr += ",";
                    jsonstr += "\"content\":\"";
                    jsonstr += request.getContent().toString();
                    jsonstr += "\"";
                    jsonstr += "}";
                }
                jsonstr += "]";

                httpRequest = HttpRequest.newBuilder()
                        .uri(uri_comitall)
                        .POST(HttpRequest.BodyPublishers.ofByteArray(jsonstr.getBytes()))
                        .build();
                HttpResponse<?> response = client.send(httpRequest, HttpResponse.BodyHandlers.discarding());
                if(response.statusCode() != 200 && settingsController.getIsVerbose()==true) {
                    System.out.println("strg manager transmitter: Error Code other than 200 was received when sending data");
                }else if(settingsController.getIsVerbose()){
                    System.out.println("strg manager transmitter: data send successfully ");
                }
            }catch (Exception e){
                if(settingsController.getIsVerbose()){
                    System.out.println("Problem in stub transmitter. Cannot transmit request");
                    e.printStackTrace();
                }
            }
        }
    }

}
