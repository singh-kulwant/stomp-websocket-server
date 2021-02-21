package com.websocket;


import com.websocket.model.AuthenticationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

    boolean authFlag;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        LOG.info("Connection established [server={}]", session.getRemoteAddress());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        LOG.info("Connection closed [server={}]", session.getRemoteAddress());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOG.info("Message received [payload={}]", message.getPayload());
        if (!authFlag) {
            applicationEventPublisher.publishEvent(new AuthenticationEvent(session));
            authFlag = true;
        }
    }

}
