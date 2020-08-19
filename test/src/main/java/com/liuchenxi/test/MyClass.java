package com.liuchenxi.test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

public class MyClass {

    static class Mutex implements Lock, java.io.Serializable {

        Sync sync =new Sync();
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
                if(getState()==0)throw new IllegalMonitorStateException();

                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }

            Condition newCondition(){
                return new ConditionObject();
            }

        }
    }
}
