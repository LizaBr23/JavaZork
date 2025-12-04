package ZorkGame.networking;

import java.io.Serializable;

public class NetworkProtocol {
    public enum MessageType {
        CONNECT, DISCONNECT, PLAYER_JOIN, PLAYER_LEAVE,
        GAME_STATE_UPDATE, PLAYER_MOVE, PLAYER_ACTION,
        ITEM_PICKUP, ITEM_DROP, CHAT_MESSAGE, PLAYER_LOCATION,
        ACK, ERROR
    }

    public static class Message implements Serializable {
        private static final long serialVersionUID = 1L;
        private MessageType type;
        private String senderId;
        private long timestamp;
        private String data;

        public Message(MessageType type, String senderId, String data) {
            this.type = type;
            this.senderId = senderId;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public MessageType getType() { return type; }
        public String getSenderId() { return senderId; }
        public String getData() { return data; }
        public long getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s", type, senderId, data);
        }
    }

    public static class ChatMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private String senderId;
        private String message;
        private boolean isGlobal;

        public ChatMessage(String senderId, String message, boolean isGlobal) {
            this.senderId = senderId;
            this.message = message;
            this.isGlobal = isGlobal;
        }

        public String getSenderId() { return senderId; }
        public String getMessage() { return message; }
        public boolean isGlobal() { return isGlobal; }
    }
}
