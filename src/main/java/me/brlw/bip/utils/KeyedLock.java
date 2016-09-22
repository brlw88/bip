package me.brlw.bip.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vl on 17.09.16.
 */
public class KeyedLock<T> {
    private final ConcurrentMap<T, Lock> keyedLocks = new ConcurrentHashMap<>();

    public Lock getLockFor(T key)
    {
        Lock ourLock = keyedLocks.get(key);
        Lock theirLock = null;
        if (ourLock == null)
            theirLock = keyedLocks.putIfAbsent(key, ourLock = new ReentrantLock());
        if (theirLock != null)
            ourLock = theirLock;
        return ourLock;
    }
}
