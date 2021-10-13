package server.inner_modules.transmitters;

import server.data_structures.ReadyLists;
import server.data_structures.SyncIORequestLinkedList;
import server.inner_modules.MeasurementController;
import server.inner_modules.SettingsController;
import server.inner_modules.StateController;
import server.inner_modules.transmitters.transmitter_types.StubTransmitter;

public class TransmitterBuilder {
    public static ParentTransmitter build(
            ReadyLists readyLists,
            StateController stateController,
            SettingsController settingsController,
            MeasurementController measurementController)
    {
        ParentTransmitter transmitter;
        String transmitterType = settingsController.getSetting("transmitter").toString();
        transmitterType = transmitterType.toUpperCase();
        switch(transmitterType){
            case "STUB":
                transmitter = new StubTransmitter();
                break;
            default:
                transmitter = new ParentTransmitter();
        }

        transmitter.setReadyLists(readyLists);
        transmitter.setStateController(stateController);
        transmitter.setMeasurementController(measurementController);
        return transmitter;
    }
}
