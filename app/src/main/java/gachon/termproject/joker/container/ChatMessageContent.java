package gachon.termproject.joker.container;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageContent {
    public Map<String, User> users = new HashMap<>();
    public Map<String, Message> messages = new HashMap<>();

    public static class User {
        public String nickname;
        public String profileImg;
    }

    public static class Message {
        public String userId;
        public String message;
        public Object timestamp;
    }
}
