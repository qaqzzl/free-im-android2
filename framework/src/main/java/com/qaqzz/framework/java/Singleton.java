package com.qaqzz.framework.java;

/**
 * FileName: Singleton
 * Founder: LiuGuiLin
 * Profile: 单例设计模式
 */

/*
 * 单例设计模式 是最简单的设计模式之一
 * 创建类型：它提供了一种创建对象的方式
 * 作用：保证对象的唯一性 ——> 一个班级可以有无数个老师，只有一个班主任，保证班主任只有一位
 * 注意：
 * 1.单例只能有一个实例
 * 2.必须自己创建实例 Object obj = new Object();
 * 3.必须提供给所有对象单例的获取当时 Object.getInstance();
 */

/*
 * 单例设计模式 是最简单的设计模式之一
 * 创建类型：它提供了一种创建对象的方式
 * 作用：保证对象的唯一性 ——> 一个班级可以有无数个老师，只有一个班主任，保证班主任只有一位
 * 注意：
 * 1.单例只能有一个实例
 * 2.必须自己创建实例 Object obj = new Object();
 * 3.必须提供给所有对象单例的获取当时 Object.getInstance();
 */


//懒汉
//public class Singleton {
//
//    private static Singleton mInstance;
//
//    //private修饰之后 new
//    private Singleton() {
////
////    }
//
//    //——> 安全
//    public static synchronized Singleton getInstance() {
//        //多线程的话 ——> 不安全
//        if (mInstance == null) {
//            mInstance = new Singleton();
//        }
//        return mInstance;
//    }
//}


//饿汉 ClassLoad 加载机制 有效的避免了多线程同步的问题
//public class Singleton {
//
//    private static Singleton mInstance ;
//
//    private Singleton(){
//        mInstance = new Singleton()
//    }
//
//    public static Singleton getInstance(){
//        return mInstance;
//    }
//}

//静态内部类 ClassLoad 加载机制


/**
 * 静态内部类的方式和饿汉的区别：
 * 饿汉：类装载了之后就实例化了对象
 * 静态内部类：类加载了之后也许还没实例化
 */

//public class Singleton {
//
//    private static class SingletonHolder {
//        private static Singleton mInstance = new Singleton();
//    }
//
//    private Singleton() {
//        testLoadData();
//    }
//
//    //加载测试数据
//    private void testLoadData() {
//        //ui thread sleep 16s
//    }


//
//    public static Singleton getInstance() {
//        return SingletonHolder.mInstance;
//    }
//}

//枚举 有效的避免多线程的同步问题 还能防止反序列化 双重保护下，类唯一，并且极度安全
//但是 他在实际工作中 出现的次数非常少
//public enum Singleton {
//
//    INSTANCE;
//
//    public void whit() {
//
//    }
//}

//双重校验锁 非常的安全
public class Singleton {
    //第二层
    private volatile static Singleton mInstance;
    private Singleton() {

    }
    //第一层
    public static Singleton getInstance() {
        if (mInstance == null) {
            synchronized (Singleton.class) {
                if (mInstance == null) {
                    mInstance = new Singleton();
                }
            }
        }
        return mInstance;
    }
}

