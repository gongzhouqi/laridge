package games.dummyGame;

import games.GameProcessor;
import services2.client.GameClientCore;

public class DummyProcessor extends GameProcessor {

    public DummyProcessor(GameClientCore client) {
        super(client);
    }

    @Override
    public void processServerOperation(String operation) {

    }
}
