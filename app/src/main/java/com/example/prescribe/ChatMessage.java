package com.example.prescribe;

import java.util.Date;

public class ChatMessage {
    private  String messageText;
    private  String MessageUser;
    private  long messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.MessageUser = messageUser;

        messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return MessageUser;
    }

    public void setMessageUser(String messageUser) {
        MessageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
