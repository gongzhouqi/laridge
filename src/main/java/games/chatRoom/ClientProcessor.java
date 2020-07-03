package games.chatRoom;

import games.GameProcessor;
import services2.client.GameClientCore;

public class ClientProcessor extends GameProcessor {
    public ClientProcessor(GameClientCore client) {
        super(client);
    }

    @Override
    public void processServerOperation(String operation) {

    }
}
