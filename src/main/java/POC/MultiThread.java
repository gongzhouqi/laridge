package POC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThread {

    List<String> connectedUsers =
//            Collections.synchronizedList(
                    new ArrayList<>()
//            )
            ;
    int count = 0;
    int max = 2;

    ReentrantLock lock = new ReentrantLock();

    public boolean join(String name) {
        lock.lock();
        System.out.println(name + " locked.");
        try {
            if (connectedUsers.size() < max) {
//                Thread.sleep(1000);
                connectedUsers.add(name);
                System.out.println(name + " is in.");
                return true;
            } else {
//                Thread.sleep(1000);
                System.out.println(name + " is not in.");
                return false;
            }
        } finally {
            System.out.println(name + " unlocked.");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MultiThread game = new MultiThread();
        Thread t1 = new Thread(() -> {
            System.out.println("NO.1 started");
            game.join("NO.1");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("NO.2 started");
            game.join("NO.2");
        });
        Thread t3 = new Thread(() -> {
            System.out.println("NO.3 started");
            game.join("NO.3");
        });
        Thread t4 = new Thread(() -> {
            System.out.println("NO.4 started");
            game.join("NO.4");
        });
        Thread t5 = new Thread(() -> {
            System.out.println("NO.5 started");
            game.join("NO.5");
        });
        Thread t6 = new Thread(() -> {
            System.out.println("NO.6 started");
            game.join("NO.6");
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

    }
}
