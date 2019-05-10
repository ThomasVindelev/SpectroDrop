package com.example.demo.Models;

public class Task {

    private int id;
    private int customer;
    private int employee;
    private int status;
    private String taskName;

    public Task(int id, int customer, int employee, int status, String taskName) {
        this.id = id;
        this.customer = customer;
        this.employee = employee;
        this.status = status;
        this.taskName = taskName;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
