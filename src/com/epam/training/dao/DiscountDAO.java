package com.epam.training.dao;


import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Discount;
import com.epam.training.entity.User;

public interface DiscountDAO extends DAO<Discount> {

	Discount findByUser(User user) throws DAOException;
}
