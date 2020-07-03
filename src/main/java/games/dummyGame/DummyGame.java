package games.dummyGame;

import games.GameCore;
import services2.server.GameServerCore;

public class DummyGame extends GameCore {

    public DummyGame(GameServerCore server, int playerNumber) {
        super(server, playerNumber);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void processInput(int player, String input) {

    }
}
