package com.example.shopkart;

public class Message {
    private String messages;
    private String sender;
    private int messageID;

    public Message(String message, String sender, int messageID){
        this.messages = message;
        this.sender = sender;
        this.messageID = messageID;
    }

    public String getMessages() {
        return messages;
    }

    public String getSender() {
        return sender;
    }

    public int getMessageID() {
        return messageID;
    }
}

