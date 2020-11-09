package com.example.myapplication;

public class DangerNode {

    private int dangernumber;
    private int dangertime;

    public DangerNode() {
        super();
    }

    public DangerNode(int dangernumber, int dangertime) {
        super();
        this.dangernumber = dangernumber;
        this.dangertime = dangertime;
    }

    public int getDangerNumber() {
        return dangernumber;
    }

    public boolean searchDangerNumber( int num) {
        return num == dangernumber;

    }

    public void setDangerNumber(int number) {
        this.dangernumber = dangernumber;
    }

    public int getDangerTime() {
        return dangertime;
    }

    public void setDangerTime(int time) {
        this.dangertime = time;
    }
}
