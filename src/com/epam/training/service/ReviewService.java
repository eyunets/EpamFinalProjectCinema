package com.epam.training.service;

import java.util.List;

import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;
import com.epam.training.service.exception.ServiceException;

public interface ReviewService extends Service<Review> {
	List<Review> findByUser(User user) throws ServiceException;

	List<Review> findByFilm(Film film) throws ServiceException;

}
