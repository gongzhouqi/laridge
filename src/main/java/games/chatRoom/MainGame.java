package games.chatRoom;

import games.GameCore;
import services2.server.GameServerCore;

public class MainGame extends GameCore {

    public MainGame(GameServerCore server, int playerNumber) {
        super(server, playerNumber);
    }

    @Override
    public void processInput(int player, String input) {

    }
}
