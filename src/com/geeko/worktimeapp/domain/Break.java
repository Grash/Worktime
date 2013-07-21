package com.geeko.worktimeapp.domain;

public class Break {

    private TimeStamp startBreak;
    private TimeStamp stopBreak;

    public Break(TimeStamp startBreak) {
        super();
        this.startBreak = startBreak;
    }

    public Break(TimeStamp startBreak, TimeStamp stopBreak) {
        super();
        this.startBreak = startBreak;
        this.stopBreak = stopBreak;
    }

    public TimeStamp getStartBreak() {
        return startBreak;
    }

    public void setStartBreak(TimeStamp startBreak) {
        this.startBreak = startBreak;
    }

    public TimeStamp getStopBreak() {
        return stopBreak;
    }

    public void setStopBreak(TimeStamp stopBreak) {
        this.stopBreak = stopBreak;
    }

    public boolean isInBreak() {
        return stopBreak == null;
    }

    public long getBreakInMinutes() {
        long breakTime;
        if (stopBreak != null) {
            breakTime = (stopBreak.getDate().getTime() - startBreak.getDate().getTime()) / 60000;
        } else {
            breakTime = 0;
        }
        return breakTime;
    }

}
