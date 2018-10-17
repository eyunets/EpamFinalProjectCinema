package com.epam.training.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.ReviewDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.ReviewDAOImpl;
import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;
import com.epam.training.service.ReviewService;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.exception.ServiceException;

public class ReviewServiceImpl extends TransactionManager implements ReviewService {
	private static volatile ReviewService INSTANCE = null;
	private ReviewDAO reviewDAO = ReviewDAOImpl.getInstance();

	private ReviewServiceImpl() {
	}

	public static ReviewService getInstance() {
		ReviewService ReviewService = INSTANCE;
		if (ReviewService == null) {
			synchronized (ReviewServiceImpl.class) {
				ReviewService = INSTANCE;
				if (ReviewService == null) {
					INSTANCE = ReviewService = new ReviewServiceImpl();
				}
			}
		}

		return ReviewService;
	}

	@Override
	public Review save(Review review) throws ServiceException {

		try {
			if (review != null) {
				startTransaction();
				review = reviewDAO.save(review);
				review = reviewDAO.updateFilmRating(review);
				commit();
				return review;
			} else {
				throw new ServiceException("Review not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating Review", e);
		}
	}

	@Override
	public Review find(Serializable id) throws ServiceException {
		Review review = new Review();
		try {
			startTransaction();
			review = reviewDAO.find(id);
			commit();
			return review;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public void update(Review review) throws ServiceException {
		try {
			startTransaction();
			reviewDAO.update(review);
			reviewDAO.updateFilmRating(review);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating Review", e);
		}
	}

	public void update(Review oldReview, Review newReview) throws ServiceException {
		Review review = new Review();
		review.setId(oldReview.getId());
		review.setUserId(oldReview.getUserId());
		review.setFilmId(oldReview.getFilmId());
		review.setDate((newReview.getDate() == null) ? oldReview.getDate() : newReview.getDate());
		review.setText((newReview.getText().length() == 0) ? oldReview.getText() : newReview.getText());
		review.setMark((newReview.getMark() == 0) ? oldReview.getMark() : newReview.getMark());
		update(review);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			Review review = reviewDAO.find(id);
			int rows = reviewDAO.delete(id);
			reviewDAO.updateFilmRating(review);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting Review", e);
		}
	}

	@Override
	public List<Review> findAll() throws ServiceException {
		ArrayList<Review> reviews;
		try {
			startTransaction();
			reviews = new ArrayList<>(reviewDAO.findAll());
			commit();
			return reviews;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Review", e);
		}
	}

	@Override
	public List<Review> findByFilm(Film film) throws ServiceException {
		ArrayList<Review> reviews;
		try {
			startTransaction();
			reviews = new ArrayList<>(reviewDAO.findByFilm(film));
			commit();
			return reviews;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Review", e);
		}
	}

	@Override
	public List<Review> findByUser(User user) throws ServiceException {
		ArrayList<Review> reviews;
		try {
			startTransaction();
			reviews = new ArrayList<>(reviewDAO.findByUser(user));
			commit();
			return reviews;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Review", e);
		}
	}

}
