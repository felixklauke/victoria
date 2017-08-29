package de.d3adspace.victoria.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaThreadFactory implements ThreadFactory {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "Victoria Worker Thread #" + atomicInteger.incrementAndGet());
    }
}
