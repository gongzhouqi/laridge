package services2.client;

import games.GameProcessor;
import games.Games;
import games.dummyGame.DummyProcessor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import services2.Core;
import services2.models.GameOperationModel;
import services2.models.GameReadyModel;
import services2.models.Model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static constants.GameConstants.*;

public class GameClientCore extends Core {

    private GameProcessor game;

    private SseEmitter waiter;

    private int serverIP;

    private int serverPort;

    private GameClientCore (String gameId, int serverIP, int serverPort) {
        Games.MetaData info = Games.GAME_INFO.get(gameId);;
        try {
            this.game = (GameProcessor) Class.forName(GAME_PACKAGE_PATH + info.getPkg() + GAME_PROCESSOR_NAME)
                    .getConstructor(getClass()).newInstance(this);
        } catch (InstantiationException
                | InvocationTargetException
                | NoSuchMethodException
                | IllegalAccessException
                | ClassNotFoundException e) {
            this.game = new DummyProcessor(this);
        }
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public static GameClientCore guestGame(String gameId, int serverIP, int serverPort) {
        return new GameClientCore(gameId, serverIP, serverPort);
    }

    @Override
    public void process(Model model, int replyIP, int replyPort) {
        if (model instanceof GameOperationModel) {
            game.processServerOperation(((GameOperationModel)model).getOperation());
        }
    }

    public void showChange(String change) {
        signalFrontEnd(change);
    }

    private void signalFrontEnd(String message) {
        if (waiter != null) {
            SseEmitter.SseEventBuilder event = SseEmitter.event().data(message);
            try {
                waiter.send(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitOn(SseEmitter waiter) {
        this.waiter = waiter;
        waiter.onCompletion(this::stopWait);
    }

    public void stopWait() {
        waiter = null;
    }

    public void tellReady() {
        send(new GameReadyModel(), serverIP, serverPort);
    }

    public void tellPlayerAction(String action) {
        send(new GameOperationModel(action), serverIP, serverPort);
    }
}
