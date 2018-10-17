package com.epam.training.db;

import java.sql.Connection;

import com.epam.training.db.exception.ConnectionPoolException;

public class ConnectionManager {
	private static ThreadLocal<ProxyConnection> tl = new ThreadLocal<>();

	public static Connection getConnection() throws ConnectionPoolException {
		if (tl.get() == null) {
			tl.set(ConnectionPool.getInstance().getConnection());
		}
		return tl.get();
	}

	public static void closeConnection() throws ConnectionPoolException {
		if (tl.get() == null) {
			ConnectionPool.getInstance().returnConnection(tl.get());
		}
	}
}