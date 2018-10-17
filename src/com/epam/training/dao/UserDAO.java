package com.epam.training.dao;

import java.math.BigDecimal;
import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;



public interface UserDAO extends DAO<User> {

	List<User> findByEmail(String email) throws DAOException;
	List<User> findByType(String type) throws DAOException;
	User addBalance(User user) throws DAOException;
	User removeBalance(User user, Ticket ticket) throws DAOException;
}
