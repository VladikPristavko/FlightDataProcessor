import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser implements Parser{
    private static final Logger logger = Logger.getLogger(FlightDataProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public List<Tickets> parse(String path) {
        File dataFolder = new File(path);
        File[] files = dataFolder.listFiles();
        objectMapper.registerModule(new JavaTimeModule());
        List<Tickets> ticketsList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                try {
                    ticketsList.add(objectMapper.readValue(file, Tickets.class));
                } catch (IOException e) {
                    logger.error("file '" + file.getName() + "' not contains valid ticket data");
                }
            }
        }
        return ticketsList;
    }
}
