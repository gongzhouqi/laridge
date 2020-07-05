package games.showTime;

import games.GameCore;
import services2.server.GameServerCore;

public class MainGame extends GameCore {

    public MainGame(GameServerCore server, int playerNumber) {
        super(server, playerNumber);
        setOnStart(() -> {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < this.playerNumber; j++) {
                    sendOutput(j, ""+i);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
            for (int j = 0; j < this.playerNumber; j++) {
                sendOutput(j, "QUIT");
            }
        });
    }
//
//    @Override
//    public void startGame() {
//    }



    @Override
    public void processInput(int player, String input) {

    }
}
