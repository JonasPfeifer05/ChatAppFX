package at.pfeifer.chatapp.logger;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Logger {
    private List<OutputStreamWriter> allOutputWriters;
    private OutputStreamWriter primaryOutputWriter;

    public Logger(OutputStream... outputs) {
        allOutputWriters = Arrays
            .stream(outputs)
            .map(OutputStreamWriter::new)
            .toList();
        primaryOutputWriter = allOutputWriters.get(0);
    }

    Logger(OutputStream primary){
        allOutputWriters = Stream
            .of(primary)
            .map(OutputStreamWriter::new)
            .toList();
        primaryOutputWriter = allOutputWriters.get(0);
    }


}
