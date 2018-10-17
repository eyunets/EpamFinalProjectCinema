package com.epam.training.service;

import java.io.Serializable;
import java.util.List;

import com.epam.training.service.exception.ServiceException;

public interface Service<T> {

	T save(T t) throws ServiceException;

	T find(Serializable id) throws ServiceException;

	void update(T t) throws ServiceException;

	int delete(Serializable id) throws ServiceException;

	List<T> findAll() throws ServiceException;
}