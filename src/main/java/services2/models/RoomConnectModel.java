package services2.models;

public class RoomConnectModel extends Model {
    private String roomId;
    private String name;
    private int headId;

    public RoomConnectModel() {}

    public RoomConnectModel(String roomId, String name, int headId) {
        this.roomId = roomId;
        this.name = name;
        this.headId = headId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }
}
