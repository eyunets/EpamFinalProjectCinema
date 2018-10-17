package com.epam.training.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.training.db.exception.ConnectionPoolException;
import com.mysql.jdbc.Driver;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConnectionPool {

    private static final int POOL_SIZE = 5;
    private static final int ATTEMPTS_LIMIT = 5;
    private static ConnectionPool connectionPool;
    private static LinkedBlockingQueue<ProxyConnection> connectionQueue;
    private static Lock lock = new ReentrantLock();
    private static AtomicBoolean instanceCreated = new AtomicBoolean();


    private ConnectionPool() throws ConnectionPoolException {
        log.debug("Connecting to db...");

        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            throw new ConnectionPoolException("Can't register JDBC driver! ", e);
        }

        int currentPoolSize = 0;
        int attemptsCount = 0;
        connectionQueue = new LinkedBlockingQueue<>(POOL_SIZE);
        while (currentPoolSize < POOL_SIZE && attemptsCount < ATTEMPTS_LIMIT) {
            try {
                ProxyConnection proxyConnection = new ProxyConnection(DBConnector.getConnection());
                connectionQueue.offer(proxyConnection);
                currentPoolSize++;
            } catch (SQLException e) {
                attemptsCount++;
                log.warn("Can't get connection", e);
            }
        }
        if (attemptsCount == ATTEMPTS_LIMIT) {
            throw new ConnectionPoolException("Exceeded the maximum number of attempts");
        }
    }

    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (connectionPool == null) {
                    connectionPool = new ConnectionPool();
                    instanceCreated.set(true);
                    log.info("Pool created.");
                }
            } catch (ConnectionPoolException e) {
                log.fatal("Can't create connection pool", e);
                throw new RuntimeException("Can't create connection pool! ", e);
            } finally {
                lock.unlock();
            }
        }
        return connectionPool;
    }

    public ProxyConnection getConnection() throws ConnectionPoolException {
        ProxyConnection connection;
        try {
            connection = connectionQueue.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Can't get connection from queue!", e);
        }
        return connection;
    }

    public void returnConnection(ProxyConnection connection) {
        connectionQueue.offer(connection);
    }

    public static void closePool() {
        int attemptsCount = 0;
        while (!connectionQueue.isEmpty() && attemptsCount < ATTEMPTS_LIMIT) {
            try {
                connectionQueue.take().closeConnection();
            } catch (SQLException e) {
                attemptsCount++;
                log.warn("Can't close connection", e);
            } catch (InterruptedException e) {
                log.error("Can't close pool", e);
            }
        }
        if (!connectionQueue.isEmpty()) {
            log.error("Can't close pool");
        }
    }

}
