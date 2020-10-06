package com.liuchenxi.test;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

public class MyClass {

    static int i = 0;

    public static void main(String[] args) {
        mCallableThreadTest mtest = new mCallableThreadTest();
        FutureTask<String> mft = new FutureTask<>(mtest);

        new Thread(mft, "TestThread").start();

        for (int j = 0; j < 10; j++) {
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
        }
        try {
            System.out.println("子线程的返回值：" + mft.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

      FutureTask<String> mfu = (FutureTask<String>) new ThreadPoolExecutor(4,4,4,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>()).submit(new mCallableThreadTest());

        ScheduledExecutorService mSE = Executors.newScheduledThreadPool(4);
        mSE.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"延迟 三秒执行");
            }
        }, 3,TimeUnit.SECONDS);

        mSE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"延迟 三秒执行");
            }
        },3,2,TimeUnit.SECONDS );

        Commodity mCM= new Commodity();
        mCM.commodity = 0;
        product mP = new product(mCM);
        Coustomer mC = new Coustomer(mCM);
        Thread mtestProduct = new Thread(new threadProduct(mP));
        Thread mtestCoustomer = new Thread(new threadCoustommer(mC));
        mtestCoustomer.start();
        mtestProduct.start();

    }

    public static class mCallableThreadTest implements Callable<String> {

        @Override
        public String call() throws Exception {
            return ++i + "";
        }
    }

    static class Mutex implements Lock, java.io.Serializable {

        Sync sync = new Sync();

        @Override
        public void lock() {
            sync.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            sync.acquireInterruptibly(1);
        }

        @Override
        public boolean tryLock() {
            return sync.tryAcquire(1);
        }

        @Override
        public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
            return sync.tryAcquireNanos(1, timeUnit.toNanos(100000));
        }

        @Override
        public void unlock() {
            sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return sync.newCondition();
        }

        private static class Sync extends AbstractQueuedSynchronizer {
            //是否处于占用状态 1表示占用，0表示释放
            protected boolean isHeldExclusively() {
                return getState() == 1;
            }

            //当状态为0时候获取锁
            public boolean tryAcquire(int acquires) {
                assert acquires == 1;
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
                return false;
            }

            // 释放锁，将状态设置为0
            protected boolean tryRelease(int releases) {
                assert releases == 1;
                if (getState() == 0) throw new IllegalMonitorStateException();

                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }

            Condition newCondition() {
                return new ConditionObject();
            }

        }
    }
}
