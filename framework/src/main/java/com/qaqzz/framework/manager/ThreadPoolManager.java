package com.qaqzz.framework.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * FileName: ThreadPoolManager
 * Founder: LiuGuiLin
 * Profile: 线程池
 */
public class ThreadPoolManager {

    //核心线程数量
    private static final int THREAD_CORE_SIZE = 5;
    private static volatile ThreadPoolManager mInstnce = null;

    private ExecutorService serviceFix;
    private ExecutorService serviceSingle;
    private ExecutorService serviceCache;
    private ScheduledExecutorService serviceSchedule;

    private ThreadPoolManager() {

        /**
         * 四个线程池的核心实现还是在ThreadPoolExecutor
         * corePoolSize：核心线程数量
         * maximumPoolSize：最大线程数量
         * keepAliveTime：超时时间
         * workQueue：任务队列
         */
        serviceFix = Executors.newFixedThreadPool(THREAD_CORE_SIZE);
        serviceSingle = Executors.newSingleThreadExecutor();
        serviceCache = Executors.newCachedThreadPool();
        serviceSchedule = Executors.newScheduledThreadPool(THREAD_CORE_SIZE);
    }

    public static ThreadPoolManager getInstance() {
        if (mInstnce == null) {
            synchronized (ThreadPoolManager.class) {
                if (mInstnce == null) {
                    mInstnce = new ThreadPoolManager();
                }
            }
        }
        return mInstnce;
    }

    /**
     * 执行newFixedThreadPool
     *
     * @param runnable
     */
    public void executeFix(Runnable runnable) {
        /**
         * FixedThreadPool 无序
         * 并且核心线程如果都有任务则等待
         * 这个FixThreadPool一般是优化线程调度
         */
        serviceFix.execute(runnable);
    }

    /**
     * 执行newSingleThreadExecutor
     *
     * @param runnable
     */
    public void executeSingle(Runnable runnable) {
        /**
         * 可以理解为FixedThreadPool的核心线程数 = 1
         * 他的作用是单线程，一般作为队列或者有序队列
         */
        serviceSingle.execute(runnable);
    }

    /**
     * 执行newCachedThreadPool
     *
     * @param runnable
     */
    public void executeCached(Runnable runnable) {
        /**
         * 首先他是无序的
         * 并且他没有核心线程，但是他有很多线程
         * 不过他有回收机制，频繁的调用线程池都会得到一个新的线程，但是新的线程
         * 如果超过了等待时间，就会回收
         * 比喻：
         * 如果我们在饭店吃饭，你吃完了，就走了。服务员收盘子
         * 但是如果你吃完了，起身去盛饭（60s）,如果60s内回来了，那你继续吃
         * 如果没有回来，服务人员认为这个餐桌已经需要收拾了
         */
        serviceCache.execute(runnable);
    }

    /**
     * 执行newScheduledThreadPool
     *
     * @param runnable
     * @param initialDelay 延长时间
     * @param period       循环时间
     */
    public void executeScheduled(Runnable runnable, long initialDelay, long period) {
        serviceSchedule.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
