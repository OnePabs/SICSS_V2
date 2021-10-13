package server.inner_modules.transmitters.transmitter_types;

import server.data_structures.IORequest;
import server.data_structures.TimeStamp;
import server.inner_modules.transmitters.ParentTransmitter;

import static server.enumerators.TIMESTAMP_NAME.TRANSMITTER_ENTRY;
import static server.enumerators.TIMESTAMP_NAME.TRANSMITTER_EXIT;

public class StubTransmitter extends ParentTransmitter {
    @Override
    public void transmit(IORequest request){
        request.addTimeStamp(TRANSMITTER_ENTRY);
        for(TimeStamp t: request.getTimeStamps()){
            measurementController.addMeasurement(t);
        }
        request.addTimeStamp(TRANSMITTER_EXIT);
        measurementController.addMeasurement(request.getTimeStamp(TRANSMITTER_EXIT));
    }
}
