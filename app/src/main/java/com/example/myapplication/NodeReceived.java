package com.example.myapplication;

public class NodeReceived {
    private int number;
    private int time;

    public NodeReceived() {
        super();
    }

    public NodeReceived(int number, int time) {
        super();
        this.number = number;
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public boolean searchNumber( int num) {
        return num == number;

    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
