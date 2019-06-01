package com.example.demo.Models;

//Lavet af Marco Pedersen og Thomas Vindelev

public class File {

    private java.io.File file;
    private int id_task;
    private String name;
    private long bytes;

    public File(java.io.File file, int id_task, String name, long bytes) {
        this.file = file;
        this.id_task = id_task;
        this.name = name;
        this.bytes = bytes;
    }

    public File() {
    }

    public java.io.File getFile() {
        return file;
    }

    public void setFile(java.io.File file) {
        this.file = file;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
