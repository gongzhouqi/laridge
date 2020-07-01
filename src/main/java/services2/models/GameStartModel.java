package services2.models;

public class GameStartModel extends Model {
    private String gameId;
    private int serverIP;
    private int serverPort;

    public GameStartModel() {
    }

    public GameStartModel(String gameId, int serverIP, int serverPort) {
        this.gameId = gameId;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getServerIP() {
        return serverIP;
    }

    public void setServerIP(int serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
