package com.epam.training.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.service.exception.ServiceException;

import lombok.extern.log4j.Log4j;
@Log4j
public abstract class TransactionManager {

	public void startTransaction() throws ServiceException {
		try {
			getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			log.error(e);
			throw new ServiceException("Set autocommint error", e);
		}
	}

	public void commit() throws ServiceException {
		try {
			getConnection().commit();
		} catch (SQLException e) {
			throw new ServiceException("Commit error", e);
		}
	}

	private Connection getConnection() throws ServiceException {
		try {
			return ConnectionManager.getConnection();
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new ServiceException("Get connection error", e);
		}
	}

	public void rollback() throws ServiceException {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			throw new ServiceException("rollback error");
		}
	}
}
