package com.example.java4main.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override public void init(EndpointConfig config) {}

    @Override
    public Message decode(String json) throws DecodeException {
        try {
            if (!willDecode(json)) throw new DecodeException(json, "Invalid chat message");
            return mapper.readValue(json, Message.class);
        } catch (IOException | IllegalArgumentException e) {
            throw new DecodeException(json, "Unable to decode chat JSON", e);
        }
    }

    @Override
    public boolean willDecode(String json) {
        if (json == null || json.length() > 2048) return false;
        try {
            JsonNode node = mapper.readTree(json);
            return node != null && node.isObject()
                    && node.has("text") && node.get("text").isTextual()
                    && !node.get("text").asText().isBlank()
                    && node.get("text").asText().length() <= 1000
                    && node.has("type") && node.get("type").canConvertToInt();
        } catch (IOException e) { return false; }
    }

    @Override public void destroy() {}
}
