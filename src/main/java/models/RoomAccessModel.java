package models;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RoomAccessModel {
    private String roomId;

    private int ownerIP;

    private int ownerPort;

    private String response;

    private ReentrantLock lock;

    private Condition waitCondition;

    public RoomAccessModel(String roomId, int ownerIP, int ownerPort) {
        this.roomId = roomId;
        this.ownerIP = ownerIP;
        this.ownerPort = ownerPort;
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

    public String getRoomId() {
        return roomId;
    }

    public int getOwnerIP() {
        return ownerIP;
    }

    public int getOwnerPort() {
        return ownerPort;
    }
}
