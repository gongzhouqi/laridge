package user;

import constants.DBConstants;
import models.GameLoadModel;
import models.GameWaitModel;
import models.RoomAccessModel;
import models.RoomWaitModel;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import services2.client.GameClientCore;
import services2.client.HallCore;
import services2.client.RoomClientCore;
import services2.server.GameServerCore;
import services2.server.RoomServerCore;
import utils.NetUtil;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class User {
    private static User singleton;

    private String username;

    private int headId;

    private HallCore hallCore;

    private RoomServerCore roomServer;

    private RoomClientCore roomClient;

    private GameServerCore gameServer;

    private GameClientCore gameClient;

    private int localRelativeIP;

    public static User getSingleton() {
        if (singleton == null) {
            singleton = new User();
        }
        return singleton;
    }

    private User() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(DBConstants.USER_DB + "/info.txt")))) {
            this.username = reader.readLine();
            this.headId = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            if (this.username == null) this.username = "...";
            if (this.headId == 0) this.headId = 1;
        }
        this.hallCore = new HallCore();
        localRelativeIP = NetUtil.getLocalAddress();
    }

    public String getUsername() {
        return username;
    }

    public String setUsername(String username) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DBConstants.USER_DB + "/info.txt")))) {
            this.username = username;
            writer.write(toString());
            return "OK";
        } catch (IOException e) {
            return "FAILED";
        }
    }

    public int getHeadId() {
        return headId;
    }

    public String setHeadId(int headId) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DBConstants.USER_DB + "/info.txt")))) {
            this.headId = headId;
            writer.write(toString());
            return "OK";
        } catch (IOException e) {
            return "FAILED";
        }
    }

    public HallCore getHall() {
        return hallCore;
    }

    public int getLocalRelativeIP() {
        return localRelativeIP;
    }

    public void hostRoom(String roomName, String gameId) {
        roomServer = RoomServerCore.hostRoomService(roomName, gameId);
        roomClient = RoomClientCore.guestRoom(
                    new RoomAccessModel(
                            roomServer.getRoomId(),
                            localRelativeIP,
                            roomServer.getPort()
                    ));
    }

    public void setRoomClient(RoomClientCore roomClient) {
        this.roomClient = roomClient;
        System.out.println("Room assigned");
    }

    public void setGameServer(GameServerCore gameServer) {
        this.gameServer = gameServer;
    }

    public void guestRoom(RoomAccessModel rm) {
        roomClient = RoomClientCore.guestRoom(rm);
    }

    public void closeRoom() {
        if (roomServer != null) {
            roomServer.shutDown();
            roomServer = null;
            broadcastCloseRoom();
        }
        if (roomClient != null) {
            roomClient.shutDown();
            roomClient = null;
        }
    }

    public void broadcastRoom() {
        if (roomServer != null) {
            hallCore.sendRoomBroadCast(
                roomServer.getRoomId(),
                username,
                roomServer.getRoomName(),
                roomServer.getGameId(),
                roomServer.getSize(),
                localRelativeIP,
                roomServer.getPort()
            );
        }
    }

    public void waitOnRoom(RoomWaitModel waiter) {
        if (roomClient == null) {
            System.out.println("Room missing");
        } else {
            roomClient.waitOn(waiter);
        }
    }

    public void waitOnGame(SseEmitter waiter) {
        if (gameClient == null) {
            System.out.println("Game missing");
        } else {
            gameClient.waitOn(waiter);
        }
    }

    public void broadcastCloseRoom() {
        hallCore.sendRoomCLose();
    }

    public String getCurrentRooms() {
        return hallCore.getCurrentRooms();
    }

    public void letGameStart() {
        if (roomServer != null) {
            List<OtherUser> users = roomServer.getCurrentUsers();
            String gameId = roomServer.getGameId();
            gameServer = GameServerCore.hostGame(gameId, users);
            roomServer.startGame(localRelativeIP, gameServer.getPort());
        } else {
            System.out.println("Room server is null, something is wrong.");
        }
    }

    public void loadGame(String gameId, int serverIP, int serverPort) {
        gameClient = GameClientCore.guestGame(gameId, serverIP, serverPort);
    }

    public void frontEndGameReady(GameLoadModel model) {
        model.getLock().lock();
        try {
            while (gameClient == null) {
                model.getWaitCondition().await(100, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException ignored) {
        } finally {
            if (gameClient != null) {
                gameClient.tellReady();
                model.setResponse("OK");
            } else {
                model.setResponse("NO");
            }
            model.getLock().unlock();
        }
    }

    @Override
    public String toString() {
        return username + "\n" + headId;
    }
}
