package models;

import java.util.Objects;
import java.util.Timer;

public class GameRoomTitle {
    private String roomId;
    private String userName;
    private int subIP;
    private String roomName;
    private String gameId;
    private int userNumber;
    private int port;
    private transient Timer timer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSubIP() {
        return subIP;
    }

    public void setSubIP(int subIP) {
        this.subIP = subIP;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public GameRoomTitle() {
    }

    public GameRoomTitle(String roomId, String userName, String roomName, String gameId, int userNumber, int subIP, int port, Timer timer) {
        this.roomId = roomId;
        this.userName = userName;
        this.subIP = subIP;
        this.roomName = roomName;
        this.gameId = gameId;
        this.userNumber = userNumber;
        this.port = port;
        this.timer = timer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoomTitle title = (GameRoomTitle) o;
        return subIP == title.subIP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subIP);
    }
}
