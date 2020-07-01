package services2.server;

import games.Games;
import services2.Core;
import services2.models.*;
import user.OtherUser;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class RoomServerCore extends Core {

    private String roomId;

    private String roomName;

    private String gameId;

    private int ownedBy;

    private List<OtherUser> connectedUsers;

    private int openedSeats;

    private Games.MetaData gameInfo;

    private ReentrantLock lock;

    private AtomicBoolean blocked;

    public static RoomServerCore hostRoomService (String roomName, String gameId) {
        return new RoomServerCore(
                User.getSingleton().getLocalRelativeIP() + "-" + System.nanoTime(),
                roomName,
                gameId,
                User.getSingleton().getLocalRelativeIP()
        );
    }

    private RoomServerCore(String roomId, String roomName, String gameId, int ownedBy) {
        this.blocked = new AtomicBoolean(false);
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameId = gameId;
        this.ownedBy = ownedBy;
        gameInfo = Games.GAME_INFO.get(gameId);
        connectedUsers = new ArrayList<>();
        int max = gameInfo.getMax();
        for (int i = 0; i < 8; i++) {
            if (i < max) {
                connectedUsers.add(OtherUser.getEmptyUser());
            } else {
                connectedUsers.add(OtherUser.getClosedUser());
            }
        }
        openedSeats = max;
        lock = new ReentrantLock();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getGameId() {
        return gameId;
    }

    public int getOwnedBy() {
        return ownedBy;
    }

    public int getSize() {
        lock.lock();
        try {
            return connectedUsers.size();
        } finally {
            lock.unlock();
        }
    }

    public List<OtherUser> getCurrentUsers() {
        lock.lock();
        try {
            return new ArrayList<>(connectedUsers);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void process(Model model, int replyIP, int replyPort) {
        lock.lock();
        try {
            if (blocked.get()) return;
            if (model instanceof RoomConnectModel) {
                processConnect((RoomConnectModel) model, replyIP, replyPort);
            }
//            else if (type.equals(ROOM_QUIT)) {
//            processGoodAccess();
//            } else if (type.equals(ROOM_REPORT)) {
                // TODO:
//            }
        } finally {
            lock.unlock();
        }
    }

    private void processConnect(RoomConnectModel model, int ip, int port) {
        if (roomId.equals(model.getRoomId())) {
            if (openedSeats > 0) {
                OtherUser emptyUser = connectedUsers.get(findEmptySeat());
                emptyUser.setUsername(model.getName());
                emptyUser.setHeadId(model.getHeadId());
                emptyUser.setSubIP(ip);
                emptyUser.setPort(port);
                send(new AccessResultModel(true), ip, port);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                } finally {
                    sendNewRoomArrange();
                }
            } else {
                send(new AccessResultModel(false), ip, port);
            }
        } else {
            send(new AccessResultModel(false), ip, port);
        }
    }

    private int findEmptySeat() {
        for (int i = 0; i < 8; i++) {
            if (connectedUsers.get(i).isEmptyUser()) {
                return i;
            }
        }
        return -1;
    }

    /*

    EMPTY_USER = "???";
    EMPTY_USER_HEAD = -1;

    CLOSED_USER = "!!!";
    CLOSED_USER_HEAD = -2;
     */
    private void sendNewRoomArrange() {
        List<String> names = new ArrayList<>();
        List<Integer> headIds = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            OtherUser user = connectedUsers.get(i);
            names.add(user.getUsername());
            headIds.add(user.getHeadId());
        }
        RoomArrangeModel model = new RoomArrangeModel(names, headIds);
        for (int i = 0; i < 8; i++) {
            OtherUser user = connectedUsers.get(i);
            if (user.isNormalUser()) {
                send(model, user.getSubIP(), user.getPort());
            }
        }
    }

    public void startGame(int ip, int port) {
        lock.lock();
        try {
            blocked.set(true);
            for (int i = 7; i >= 0; i--) {
                OtherUser user = connectedUsers.get(i);
                if (user.isNormalUser()) {
                    send(new GameStartModel(gameId, ip, port), user.getSubIP(), user.getPort());
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
