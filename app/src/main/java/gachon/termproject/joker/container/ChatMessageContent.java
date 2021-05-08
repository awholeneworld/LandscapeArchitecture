package gachon.termproject.joker.container;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageContent {
    public static class Message {
        public String userId;
        public String message;
        public Object timestamp;
    }

    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Message> messages = new HashMap<>();
}
