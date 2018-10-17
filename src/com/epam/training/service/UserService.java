package com.epam.training.service;

import java.util.List;

import com.epam.training.entity.Film;
import com.epam.training.entity.User;
import com.epam.training.service.exception.ServiceException;

public interface UserService extends Service<User> {

	User findByEmail(String email) throws ServiceException;

	List<User> findByType(String type) throws ServiceException;

	List<User> findByFilm(Film film) throws ServiceException;

	void update(User sessionUser, User newUser) throws ServiceException;

	User addBalance(User user) throws ServiceException;

}
