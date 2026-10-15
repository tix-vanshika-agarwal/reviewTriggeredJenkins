package com.tiket.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedPool {

    public static final ExecutorService POOL = Executors.newCachedThreadPool();
    private static final Logger logger = LogManager.getLogger(CachedPool.class);

    public static void close() {
        if (!POOL.isShutdown()) {
            logger.debug("Shutting down thread pool");
            POOL.shutdown();
        }
    }
}
