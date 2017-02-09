package main;

import static main.Main.lock;

public class Main {
    private static final int ITER_COUNTER = 1000;
    private static final int ITER_MESSAGER_1 = 5;
    private static final int ITER_MESSAGER_2 = 7;
    public static long time;
    public static final Object lock = new Object();
    public static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        Thread counter = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    time = System.currentTimeMillis() - startTime;
                    //System.out.println(time);
                    synchronized (lock){
                        lock.notifyAll();
                    }
                    try{
                        Thread.sleep(ITER_COUNTER);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread messager1 = new Thread(new Messager(ITER_MESSAGER_1));
        messager1.setName("5sec messager");
        Thread messager2 = new Thread(new Messager(ITER_MESSAGER_2));
        messager2.setName("7sec messager");

        messager1.start();
        messager2.start();
        counter.start();
    }
}

class Messager implements Runnable {
    private int count = 0;
    private final int iter;

    public Messager(int iter){
        this.iter = iter;
    }

    @Override
    public void run() {
        while(true){
            try {
                synchronized (lock) {
                    lock.wait();
                    if (++count%iter == 0){
                        System.out.println(Main.time + " - by " + Thread.currentThread().getName());
                    }
                    }
                //Thread.sleep(iter);
                //lock.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
