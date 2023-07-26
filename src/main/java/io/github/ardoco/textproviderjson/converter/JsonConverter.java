/* Licensed under MIT 2023. */
package io.github.ardoco.textproviderjson.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import io.github.ardoco.textproviderjson.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * utility class to convert a text DTO into json and back
 **/
public final class JsonConverter {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    private static final String SCHEMA_PATH = "schemas/text.json";

    private JsonConverter() {

    }

    /***
     * checks whether the json string matches the text schema
     * 
     * @param json the json string
     * @return whether the json string matches the text schema
     */
    public static boolean validateJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

        InputStream inputSchema = JsonConverter.class.getClassLoader().getResourceAsStream(SCHEMA_PATH);
        JsonSchema schema = schemaFactory.getSchema(inputSchema);

        Set<ValidationMessage> message = schema.validate(mapper.readTree(json));
        if (!message.isEmpty()) {
            // get only the first fifteen messages
            List<String> loggerMessages = message.stream().map(ValidationMessage::getMessage).toList();
            String loggerMessage = String.join("\n", loggerMessages.subList(0, 15));
            logger.info("The following inconsistencies between the json and the json schema were found: {}", loggerMessage);
        }
        return message.isEmpty();
    }

    /**
     * generates the corresponding text DTO of the json string
     * 
     * @param json the json string
     * @return the corresponding text DTO
     */
    public static TextDTO fromJsonString(String json) throws IOException {
        if (validateJson(json)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, TextDTO.class);
        }
        logger.warn("The json string could not be validated.");
        return null;
    }

    /**
     * converts the text DTO into json string. Returns null if the JSON could not be validated.
     * 
     * @param obj the text DTO
     * @return the JSON string or null
     */
    public static String toJsonString(TextDTO obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(obj);
        if (!validateJson(jsonString)) {
            logger.warn("The text DTO could not be converted into a json string.");
            return null;
        }
        return jsonString;
    }
}
