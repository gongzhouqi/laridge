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
        boolean[] inputConsumed = new boolean[4];
        boolean[] ansConsumed = new boolean[4];
        for (int i = 0; i < 4; i++) {
            if (ans.charAt(i) == input.charAt(i)) {
                NP++;
                ansConsumed[i] = true;
                inputConsumed[i] = true;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (!inputConsumed[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!ansConsumed[j]) {
                        if (input.charAt(i) == ans.charAt(j)) {
                            N++;
                            ansConsumed[j] = true;
                        }
                    }
                }
            }
        }
        String info;
        if (NP == 4) {
            info = "WIN恭喜" + server.getName(prev) + "胜利！答案为：" + input + "。" + ans;
        } else {
            info =  "第" + countDown + "回合："
                    + server.getName(prev) + "猜了 " + input + " 。"
                    + NP + "个全对，" + N + "个半对。";
            countDown++;
            prev++;
            prev %= playerNumber;
        }
        for (int i = 0; i < playerNumber; i++) {
            server.sendToPlayer(i, info);
        }
        if (countDown == 11) {
            for (int i = 0; i < playerNumber; i++) {
                server.sendToPlayer(i, "TUP10回合到！正确答案为" + ans + "。游戏结束，平局！");
            }
        } else if (NP != 4) {
            tellTurn();
        }
    }
}
