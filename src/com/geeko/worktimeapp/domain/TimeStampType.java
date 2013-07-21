package com.geeko.worktimeapp.domain;

public enum TimeStampType {
    START_WORK(0), START_BREAK(1), STOP_BREAK(2), STOP_WORK(3);

    private int type;

    private TimeStampType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
