package com.aurea.deadcode.service.integration;

import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Workaround for UDB file opening limitation: it can be done from a single thread only.
 * Created by ilshat on 30.03.17.
 */
class DatabaseFactory {
    private Lock dbOpenLock = new ReentrantLock();

    private final String[] filenameHolder = new String[1];
    private final Database[] dbHolder = new Database[1];

    DatabaseFactory() {
        new DatabaseOpener().start();
    }

    Database getDatabase(String filename) {
        dbOpenLock.lock();
        try {
            setFilename(filename);
            return waitForDatabase();
        } finally {
            dbOpenLock.unlock();
        }
    }

    private void setFilename(String filename) {
        synchronized (filenameHolder) {
            filenameHolder[0] = filename;
            filenameHolder.notifyAll();
        }
    }

    private Database waitForDatabase() {
        synchronized (dbHolder) {
            while (dbHolder[0] == null) {
                try {
                    dbHolder.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Database database = dbHolder[0];
        dbHolder[0] = null;
        return database;
    }

    private class DatabaseOpener extends Thread {
        @Override
        public void run() {
            while (true) {
                String filename = waitForFilename();
                createDatabase(filename);
            }
        }

        private void createDatabase(String filename) {
            synchronized (dbHolder) {
                try {
                    dbHolder[0] = Understand.open(filename);
                } catch (UnderstandException e) {
                    throw new RuntimeException("Could not open UDB file");
                }
                dbHolder.notifyAll();
            }
        }

        private String waitForFilename() {
            synchronized (filenameHolder) {
                while (filenameHolder[0] == null) {
                    try {
                        filenameHolder.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            String filename = filenameHolder[0];
            filenameHolder[0] = null;
            return filename;
        }
    }
}
