package POC;

public class ThreadOccupyPOC {

    public void tenSleep() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10000);
                System.out.println(i+"done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shout() {
        System.out.println("!!!!!!!!!!!");
    }

}
