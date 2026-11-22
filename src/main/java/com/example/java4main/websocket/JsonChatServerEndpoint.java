package com.example.java4main.websocket;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/json/chat/{username}",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)
public class JsonChatServerEndpoint {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private void broadcast(Message message) {
        sessions.forEach((username, session) -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        if (username == null || username.isBlank()) {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "Please login before joining the chat"
            ));
            return;
        }

        String currentUsername = username;

        session.getUserProperties().put("username", currentUsername);
        sessions.put(session.getId(), session);
        broadcast(new Message("joined the chat", 0, currentUsername, sessions.size()));
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        String username = (String) session.getUserProperties().get("username");
        if (username == null) {
            return;
        }

        message.setSender(username);
        message.setType(2);
        message.setCount(null);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        if (username == null) {
            return;
        }

        sessions.remove(username);
        sessions.remove(session.getId());
        broadcast(new Message("left the chat", 1, username, sessions.size()));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to close session", e);
        }
    }
}
