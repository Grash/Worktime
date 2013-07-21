package com.geeko.worktimeapp.domain;

public enum WorkingStatus {
    OUT_OF_OFFICE(0), WORK(1), BREAK(2);

    private int status;

    private WorkingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status){
    	this.status = status;
    }

}
