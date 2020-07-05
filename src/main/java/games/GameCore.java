package games;

import services2.server.GameServerCore;

public abstract class GameCore {

    private Runnable onStart;

    private Runnable onComplete;

    private Runnable onError;

    protected final GameServerCore server;

    protected int playerNumber;

    public GameCore(GameServerCore server, int playerNumber) {
        this.server = server;
        this.playerNumber = playerNumber;
    }

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public void setOnError(Runnable onError) {
        this.onError = onError;
    }

    public void startGame() {
        if (onStart != null) {
            Thread t = new Thread(onStart);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }
    }

    public final void completeGame() {
        if (onComplete != null) {
            Thread t = new Thread(onComplete);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }
    }

    public final void handleError() {
        if (onError != null) {
            Thread t = new Thread(onError);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }
    }

    public abstract void processInput(int player, String input);

    public final void sendOutput(int player, String output) {
        server.sendToPlayer(player, output);
    }
}
