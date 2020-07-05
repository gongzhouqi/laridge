package games;

import services2.client.GameClientCore;

public abstract class GameProcessor {

    protected GameClientCore client;

    public GameProcessor(GameClientCore client) {
        this.client = client;
    }

    public abstract void processServerOperation(String operation);

}
