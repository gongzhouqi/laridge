package games;

import services2.client.GameClientCore;
import services2.server.GameServerCore;

public abstract class GameProcessor {

    protected GameClientCore client;

    public GameProcessor(GameClientCore client) {
        this.client = client;
    }

    public abstract void processServerOperation(String operation);


}
