package taxor.handler;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WSHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws ParseException, IOException {
        String payload = message.getPayload();
        System.out.println("msg" + payload);
        LinkedHashMap<String, Object> map = new JSONParser(payload).parseObject();
        String type = (String) map.get("type");
        if ("session".equals(type)) {
            sessionMap.put((String) map.get("id"), session);
        } else {
            sessionMap.get(map.get("to")).sendMessage(message);
        }
        System.out.println("sess map=>" + sessionMap);
    }

    synchronized public void call(String msg) throws IOException, ParseException {
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = new JSONParser(msg).parseObject();
        String to = (String) stringObjectLinkedHashMap.get("to");
        System.out.println(to);
        sessionMap.get(to).sendMessage(new TextMessage(msg));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.values().removeIf(session::equals);
        System.out.println("conn closed session count =>" + sessionMap.size());
    }
}
