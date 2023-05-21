package models;

import java.sql.Timestamp;

public class Customer {
    private int no;
    private String phoneNumber;
    private int peopleCount;
    private String state;
    private Timestamp time;

    public Customer() {}

    public Customer(int no, String phoneNumber, int peopleCount, String state, Timestamp time) {
        this.no = no;
        this.phoneNumber = phoneNumber;
        this.peopleCount = peopleCount;
        this.state = state;
        this.time = time;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}