package com.epam.training.dao;

import java.io.Serializable;
import java.util.List;

import com.epam.training.dao.exception.DAOException;

public interface DAO<T> {

	T save(T t) throws DAOException ;

	T find(Serializable id) throws DAOException;

	void update(T t) throws DAOException;

	int delete(Serializable id) throws DAOException;

	List<T> findAll() throws DAOException;

}