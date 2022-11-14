package net.ontopsolutions.customer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilityJackson {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parser(String body, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(body, clazz);
    }

    public static String parserString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
