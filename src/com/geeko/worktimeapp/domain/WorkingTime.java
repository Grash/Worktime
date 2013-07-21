package com.geeko.worktimeapp.domain;

public class WorkingTime {

    int hours;
    int minutes;

    public WorkingTime() {
        super();
    }

    public WorkingTime(int hours, int minutes) {
        super();
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

}
