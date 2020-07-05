package services2.server;

import games.GameCore;
import games.Games;
import games.dummyGame.DummyGame;
import services2.Core;
import services2.models.GameOperationModel;
import services2.models.GameReadyModel;
import services2.models.Model;
import user.OtherUser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static constants.GameConstants.*;

public class GameServerCore extends Core {

    private GameCore game;

    private List<OtherUser> connectedUsers;

    private Map<Integer, Integer> ipToNumber;

    private int playerNumber;

    private int readyPlayer;

    private ReentrantLock lock;

    private GameServerCore(String gameId, List<OtherUser> connectedUsers) {
        Games.MetaData info = Games.GAME_INFO.get(gameId);
        this.connectedUsers = new ArrayList<>();
        this.ipToNumber = new HashMap<>();
        for (OtherUser user : connectedUsers) {
            if (user.isNormalUser()) {
                this.ipToNumber.put(user.getSubIP(), this.connectedUsers.size());
                this.connectedUsers.add(new OtherUser(user.getUsername(), user.getHeadId(), user.getSubIP(), user.getPort()));
            }
        }
        this.playerNumber = this.connectedUsers.size();
        try {
            this.game = (GameCore) Class.forName(GAME_PACKAGE_PATH + info.getPkg() + GAME_CLASS_NAME)
                    .getConstructor(getClass(), int.class).newInstance(this, playerNumber);
        } catch (InstantiationException
                | InvocationTargetException
                | NoSuchMethodException
                | IllegalAccessException
                | ClassNotFoundException e) {
            this.game = new DummyGame(this, playerNumber);
        }
        this.readyPlayer = 0;
        this.lock = new ReentrantLock();
    }

    public static GameServerCore hostGame(String gameId, List<OtherUser> connectedUsers) {
        return new GameServerCore(gameId, connectedUsers);
    }

    @Override
    public void process(Model model, int replyIP, int replyPort) {
        lock.lock();
        try {
            if (model instanceof GameOperationModel) {
                processGameOperation((GameOperationModel)model, replyIP);
            } else if (model instanceof GameReadyModel) {
                processGameReady(replyIP, replyPort);
            }
        } finally {
            lock.unlock();
        }
    }

    private void processGameReady(int ip, int port) {
        for (OtherUser user : connectedUsers) {
            if (user.getSubIP() == ip) {
                user.setPort(port);
                break;
            }
        }
        readyPlayer++;
        if (readyPlayer == playerNumber) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            for (OtherUser user : connectedUsers) {
                send(new GameOperationModel("GO"), user.getSubIP(), user.getPort());
            }
            gameStart();
        }
    }

    private void processGameOperation(GameOperationModel model, int ip) {
        game.processInput(ipToNumber.get(ip), model.getOperation());
    }

    private void gameStart() {
        game.startGame();
    }

    public void sendToPlayer(int player, String info) {
        OtherUser user = connectedUsers.get(player);
        send(new GameOperationModel(info), user.getSubIP(), user.getPort());
    }

    public String getName(int player) {
        return connectedUsers.get(player).getUsername();
    }
}
