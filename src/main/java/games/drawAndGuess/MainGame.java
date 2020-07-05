package games.drawAndGuess;

import games.GameCore;
import services2.server.GameServerCore;

public class MainGame extends GameCore {
    public MainGame(GameServerCore server, int playerNumber) {
        super(server, playerNumber);
        setOnStart(() -> sendOutput(0, "ENABLE"));
    }

    @Override
    public void processInput(int player, String input) {
        if (input.startsWith("DRAW")) {
            for (int i = 0; i < playerNumber; i++) {
                if (i != player) {
                    sendOutput(i, input);
                }
            }
        }
    }
}
