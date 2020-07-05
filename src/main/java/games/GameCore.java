package games;

import services2.server.GameServerCore;

public abstract class GameCore {

    private GameRunnable onStart;

    private GameRunnable onComplete;

    private GameRunnable onError;

    protected final GameServerCore server;

    protected int playerNumber;

    public GameCore(GameServerCore server, int playerNumber) {
        this.server = server;
        this.playerNumber = playerNumber;
    }

    public interface GameRunnable {
        void run();
    }

    public final void setOnStart(GameRunnable onStart) {
        this.onStart = onStart;
    }

    public final void setOnComplete(GameRunnable onComplete) {
        this.onComplete = onComplete;
    }

    public final void setOnError(GameRunnable onError) {
        this.onError = onError;
    }

    public final void startGame() {
        if (onStart != null) {
            onStart.run();
        }
    }

    public final void completeGame() {
        if (onComplete != null) {
            onComplete.run();
        }
    }

    public final void handleError() {
        if (onError != null) {
            onError.run();
        }
    }

    public abstract void processInput(int player, String input);

    public final void sendOutput(int player, String output) {
        server.sendToPlayer(player, output);
    }
}
