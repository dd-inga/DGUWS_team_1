package models;

import java.sql.Timestamp;

public class Customer {
    private int no;
    private String phoneNumber;
    private int peopleCount;
    private String state;
    private Timestamp time;
    private boolean messageDelivered;
    private int waitingNumber;

    public Customer() {}

    public Customer(int no, String phoneNumber, int peopleCount, String state, Timestamp time, boolean messageDelivered, int waitNum) {
        this.no = no;
        this.phoneNumber = phoneNumber;
        this.peopleCount = peopleCount;
        this.state = "대기";
        this.time = time;
        this.messageDelivered = false;
        this.waitingNumber = waitNum+1;
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

    public boolean isMessageDelivered() {
        return messageDelivered;
    }

    public void setMessageDelivered(boolean messageDelivered) {
        this.messageDelivered = messageDelivered;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }

    public void setWaitingNumber(int num) {
        this.waitingNumber = num;
    }
}