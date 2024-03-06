package wisteria.cvapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TestHelpers {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String objectToJson(Object o) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

    }
}
