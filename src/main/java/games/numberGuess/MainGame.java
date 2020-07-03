package games.numberGuess;

import games.GameCore;
import services2.server.GameServerCore;

import java.util.Random;

public class MainGame extends GameCore {

    private int pointer = 0;

    private int prev = 0;

    private String ans;

    private int countDown = 1;

    public MainGame(GameServerCore server, int playerNumber) {
        super(server, playerNumber);
    }

    @Override
    public void startGame() {
        System.out.println(playerNumber);
        Random rand = new Random();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String next = "" + rand.nextInt(10);
            while (ans.toString().contains(next)) {
                next = "" + rand.nextInt(10);
            }
            ans.append(next);
        }
        this.ans = ans.toString();
        tellTurn();
    }

    private void tellTurn() {
        server.sendToPlayer(pointer, "ENABLE");
        pointer++;
        pointer %= playerNumber;
    }

    @Override
    public void processInput(int player, String input) {
        int NP = 0;
        int N = 0;
        for (int i = 0; i < 4; i++) {
            if (ans.charAt(i) == input.charAt(i)) {
                NP++;
            } else if (ans.contains(""+input.charAt(i))) {
                N++;
            }
        }
        String info;
        if (NP == 4) {
            info = "恭喜" + server.getName(prev) + "胜利！答案为：" + input + "。" + ans;
        } else {
            info = server.getName(prev) + "猜了 " + input + " 。" + NP + "个全对，" + N + "个半对。";
            prev++;
            prev %= playerNumber;
        }
        for (int i = 0; i < playerNumber; i++) {
            server.sendToPlayer(i, info);
        }
        if (NP != 4) {
            tellTurn();
        }
    }
}
