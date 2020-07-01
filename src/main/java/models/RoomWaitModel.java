package models;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RoomWaitModel {
    private String response;

    private ReentrantLock lock;

    private Condition waitCondition;

    public RoomWaitModel() {
        lock = new ReentrantLock();
        waitCondition = lock.newCondition();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Condition getWaitCondition() {
        return waitCondition;
    }
}
