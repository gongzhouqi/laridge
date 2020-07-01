package services2.models;

public class RoomBroadCastModel extends Model {
    private String roomId;
    private String username;
    private String roomName;
    private String gameId;
    private int size;
    private int roomIP;
    private int roomPort;

    public RoomBroadCastModel() {
    }

    public RoomBroadCastModel(String roomId, String username, String roomName, String gameId, int size, int roomIP, int roomPort) {
        this.roomId = roomId;
        this.username = username;
        this.roomName = roomName;
        this.gameId = gameId;
        this.size = size;
        this.roomIP = roomIP;
        this.roomPort = roomPort;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRoomIP() {
        return roomIP;
    }

    public void setRoomIP(int roomIP) {
        this.roomIP = roomIP;
    }

    public int getRoomPort() {
        return roomPort;
    }

    public void setRoomPort(int roomPort) {
        this.roomPort = roomPort;
    }
}
