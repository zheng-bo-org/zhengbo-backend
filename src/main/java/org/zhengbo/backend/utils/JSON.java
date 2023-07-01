package org.zhengbo.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSON {
    private static final Logger log = LoggerFactory.getLogger(JSON.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object o) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert the object to json. The clz of the object is {} and the value is {}. The error is: ", o.getClass().getName(), o, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, clz);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert the json to target clz. The json is: {} and the clz is {}. The error is: ", json, clz.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
