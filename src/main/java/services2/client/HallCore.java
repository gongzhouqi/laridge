package services2.client;

import com.google.gson.Gson;
import constants.HallServiceConstants;
import constants.WebConstants;
import models.GameRoomTitle;
import services2.Core;
import services2.models.Model;
import services2.models.RoomBroadCastModel;
import services2.models.RoomCloseModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

public class HallCore extends Core {

    private Map<Integer, GameRoomTitle> gameRoomTitleMap;

    private ReentrantLock lock;

    public HallCore() {
        super(HallServiceConstants.HALL_SERVICE_PORT);
        gameRoomTitleMap = Collections.synchronizedMap(new HashMap<>());
        lock = new ReentrantLock();
    }

    @Override
    public void process(Model model, int replyIP, int replyPort) {
        lock.lock();
        try {
            if (model instanceof RoomBroadCastModel) {
                processBroadCast((RoomBroadCastModel) model, replyIP);
            } else if (model instanceof RoomCloseModel) {
                processRoomClose((RoomCloseModel) model, replyIP, replyPort);
            }
        } finally {
            lock.unlock();
        }
    }

    private void processBroadCast(RoomBroadCastModel model, int ip) {
        if (gameRoomTitleMap.containsKey(ip)) {
            GameRoomTitle oldTitle = gameRoomTitleMap.remove(ip);
            oldTitle.getTimer().cancel();
        }
        GameRoomTitle newTitle = new GameRoomTitle(
                model.getRoomId(),
                model.getUsername(),
                model.getRoomName(),
                model.getGameId(),
                model.getSize(),
                model.getRoomIP(),
                model.getRoomPort(),
                null
                );
        gameRoomTitleMap.put(ip, newTitle);
        Timer t = new Timer();
        newTitle.setTimer(t);
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        gameRoomTitleMap.remove(ip);
                    }
                },
                30000
        );
    }

    private void processRoomClose(RoomCloseModel model, int ip, int port) {
        if (gameRoomTitleMap.containsKey(ip)) {
            GameRoomTitle oldTitle = gameRoomTitleMap.remove(ip);
            oldTitle.getTimer().cancel();
        }
    }

    public String getCurrentRooms() {
        lock.lock();
        try {
            Gson g = new Gson();
            return g.toJson(gameRoomTitleMap.values());
        } finally {
            lock.unlock();
        }
    }

    public void sendRoomBroadCast(String roomId, String username, String roomName, String gameId, int size, int ip, int port) {
        RoomBroadCastModel model = new RoomBroadCastModel(roomId, username, roomName, gameId, size, ip, port);
        send(model, WebConstants.BROADCAST_SUFFIX, HallServiceConstants.HALL_SERVICE_PORT);
        send(model, WebConstants.BROADCAST_SUFFIX, 18080);
    }

    public void sendRoomCLose() {
        send(new RoomCloseModel(), WebConstants.BROADCAST_SUFFIX, HallServiceConstants.HALL_SERVICE_PORT);
    }
}
