package com.application.threads;

import java.time.Instant;

public class ObjWrapper {

    public void getId(Object obj) {
        obj.hashCode();
    }

    public static void wait(Object obj)throws InterruptedException {
        System.out.println("Entering Object::wait: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
        obj.wait();
        System.out.println("Exiting Object::wait: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
    }

    public static void notify(Object obj) {
        System.out.println("Entering Object::notify: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
        obj.notify();
        System.out.println("Exiting Object::notify: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
    }

    public static void notifyAll(Object obj) {
        System.out.println("Entering Object::notifyAll: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
        obj.notifyAll();
        System.out.println("Exiting Object::notifyAll: Thread Name: " + Thread.currentThread() + " at time: " + Instant.now());
    }

    // For best performance, compare fewer values.
    // Overriding hash method will render it un-overrideable for the users. Therefore use a different method to establish equality.
    // Should not depend on the overridden implementation of equals, hash or toString.
}
