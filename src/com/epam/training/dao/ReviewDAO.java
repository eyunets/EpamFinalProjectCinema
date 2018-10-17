package com.epam.training.dao;
import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;

public interface ReviewDAO extends DAO<Review> {

	List<Review> findByUser(User user) throws DAOException;

	List<Review> findByFilm(Film film) throws DAOException;

	Review updateFilmRating(Review review) throws DAOException;

}
