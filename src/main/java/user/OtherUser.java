package user;

public class OtherUser {
    private static final String EMPTY_USER = "???";
    private static final int EMPTY_USER_HEAD = -1;

    private static final String CLOSED_USER = "!!!";
    private static final int CLOSED_USER_HEAD = -2;

    private String username;
    private int headId;
    private int subIP;
    private int port;

    public OtherUser(String username, int headId, int subIP, int port) {
        this.username = username;
        this.headId = headId;
        this.subIP = subIP;
        this.port = port;
    }

    public static OtherUser getEmptyUser() {
        return new OtherUser(EMPTY_USER, EMPTY_USER_HEAD, 0, 0);
    }

    public static OtherUser getClosedUser() {
        return new OtherUser(CLOSED_USER, CLOSED_USER_HEAD, 0, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSubIP() {
        return subIP;
    }

    public void setSubIP(int subIP) {
        this.subIP = subIP;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEmptyUser() {
        return username.equals(EMPTY_USER);
    }

    public boolean isClosedUser() {
        return username.equals(CLOSED_USER);
    }

    public boolean isNormalUser() {
        return !isEmptyUser() && !isClosedUser();
    }
}
