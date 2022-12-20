package com.example.schoolproject.Modal;

public class StatisticsModal {
    private int day;
    private int cnt;

    public StatisticsModal(int day, int cnt) {
        this.day = day;
        this.cnt = cnt;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
