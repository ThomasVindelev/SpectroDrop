package com.example.demo.Models;

public class Task {

    private int id;
    private int fk_customer;
    private int fk_employee;
    private int fk_status;
    private String customer;
    private String employee;
    private String status;
    private String taskName;

    public Task() {
    }

    public Task(int id, int fk_customer, int fk_employee, int fk_status, String customer, String employee, String status, String taskName) {
        this.id = id;
        this.fk_customer = fk_customer;
        this.fk_employee = fk_employee;
        this.fk_status = fk_status;
        this.customer = customer;
        this.employee = employee;
        this.status = status;
        this.taskName = taskName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_customer() {
        return fk_customer;
    }

    public void setFk_customer(int fk_customer) {
        this.fk_customer = fk_customer;
    }

    public int getFk_employee() {
        return fk_employee;
    }

    public void setFk_employee(int fk_employee) {
        this.fk_employee = fk_employee;
    }

    public int getFk_status() {
        return fk_status;
    }

    public void setFk_status(int fk_status) {
        this.fk_status = fk_status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
