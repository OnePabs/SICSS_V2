package server.inner_modules.transmitters;

import server.data_structures.ReadyLists;
import server.inner_modules.MeasurementController;
import server.inner_modules.*;
import server.inner_modules.transmitters.transmitter_types.*;

public class TransmitterBuilder {
    public static ParentTransmitter build(
            ReadyLists readyLists,
            StateController stateController,
            SettingsController settingsController,
            TransmitionInformationObject transmitionInformationObject,
            MeasurementController measurementController
            ){
        ParentTransmitter transmitter;
        String transmitterType = settingsController.getSetting("transmitter").toString();
        transmitterType = transmitterType.toUpperCase();
        switch(transmitterType){
            case "STUB":
                transmitter = new StubTransmitter(stateController, settingsController, readyLists, transmitionInformationObject, measurementController);
                //System.out.println("Builing Stub Transmitter");
                break;
            case "STORAGEMANAGERTRANSMITTER":
                transmitter = new StorageManagerTransmitter(stateController, settingsController, readyLists, transmitionInformationObject, measurementController);
                //System.out.println("Builing Storage Manager Transmitter");
                break;
            default:
                transmitter = new ParentTransmitter(stateController, settingsController, readyLists, transmitionInformationObject, measurementController);
                //System.out.println("Builing Parent Transmitter: ERROR!!!");
        }
        return transmitter;
    }
}
