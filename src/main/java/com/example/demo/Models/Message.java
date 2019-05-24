package com.example.demo.Models;

public class Message {

    private int id;
    private String text;
    private int fk_sent_to;
    private int fk_sent_from;
    private String sent_to;
    private String sent_from;
    private boolean isRead;

    public Message(int id, String text, int fk_sent_to, int fk_sent_from, String sent_to, String sent_from, boolean isRead) {
        this.id = id;
        this.text = text;
        this.fk_sent_to = fk_sent_to;
        this.fk_sent_from = fk_sent_from;
        this.sent_to = sent_to;
        this.sent_from = sent_from;
        this.isRead = isRead;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSent_to() {
        return sent_to;
    }

    public void setSent_to(String sent_to) {
        this.sent_to = sent_to;
    }

    public String getSent_from() {
        return sent_from;
    }

    public void setSent_from(String sent_from) {
        this.sent_from = sent_from;
    }

    public int getFk_sent_to() {
        return fk_sent_to;
    }

    public void setFk_sent_to(int fk_sent_to) {
        this.fk_sent_to = fk_sent_to;
    }

    public int getFk_sent_from() {
        return fk_sent_from;
    }

    public void setFk_sent_from(int fk_sent_from) {
        this.fk_sent_from = fk_sent_from;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }
}
