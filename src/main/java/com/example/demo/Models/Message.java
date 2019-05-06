package com.example.demo.Models;

public class Message {

    private int id;
    private String text;
    private int sent_to;
    private int sent_from;

    public Message(int id, String text, int sent_to, int sent_from) {
        this.id = id;
        this.text = text;
        this.sent_to = sent_to;
        this.sent_from = sent_from;
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

    public int getSent_to() {
        return sent_to;
    }

    public void setSent_to(int sent_to) {
        this.sent_to = sent_to;
    }

    public int getSent_from() {
        return sent_from;
    }

    public void setSent_from(int sent_from) {
        this.sent_from = sent_from;
    }
}
