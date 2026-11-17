package com.example.java4main.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public Message decode(String json) throws DecodeException {
        try {
            return mapper.readValue(json, Message.class);
        } catch (IOException e) {
            throw new DecodeException(json, "Unable to decode message", e);
        }
    }

    @Override
    public boolean willDecode(String json) {
        return json != null && json.contains("type") && json.contains("text");
    }

    @Override
    public void destroy() {
    }
}
