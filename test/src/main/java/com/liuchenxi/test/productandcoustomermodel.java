package com.liuchenxi.test;

import static java.lang.Thread.sleep;

class product {
    Commodity mc;

    product(Commodity mcm) {
        mc = mcm;
    }

    void addCommodity() {
        synchronized (mc) {
            if(mc.commodity==0){
                mc.commodity = mc.commodity + 1;
                System.out.println("当前product" + mc.commodity);
            }else {
                mc.notify();
            }
        }


    }
}

class Coustomer {
    Commodity mc;

    Coustomer(Commodity mcm) {
        mc = mcm;
    }

    void reduceCommodity() {
        synchronized (mc) {
            if(mc.commodity>0) {
                mc.commodity--;
                System.out.println("当前Coustomer" + mc.commodity);
            }else {
                try {
                    mc.wait();
                } catch (InterruptedException e) {
                    System.out.println("当前Coustomer 出错*****************");
                    e.printStackTrace();
                }
            }
        }
    }
}

class Commodity {
    int commodity;
}

class threadProduct implements Runnable {
    product mp;

    threadProduct(product mproduct) {
        mp = mproduct;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                mp.addCommodity();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class threadCoustommer implements Runnable {
    Coustomer mC;

    threadCoustommer(Coustomer mCoustomer) {
        mC = mCoustomer;
    }

    @Override
    public void run() {
        while (true) {
                mC.reduceCommodity();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
