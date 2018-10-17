package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.training.dao.ReviewDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;
import com.epam.training.util.SqlDateConverter;

import lombok.extern.log4j.Log4j;

@Log4j
public class ReviewDAOImpl extends SqlDateConverter implements ReviewDAO {
	private static final String SAVE_REVIEW_QUERY = "INSERT INTO REVIEW (r_user_id, r_film_id, r_date, r_mark, r_text) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_REVIEW_QUERY = "UPDATE REVIEW SET r_user_id=?, r_film_id=?, r_date=?, r_mark=?, r_text = ? WHERE r_id=?";
	private static final String SELECT_REVIEW_QUERY = "SELECT * FROM REVIEW WHERE r_id=?";
	private static final String SELECT_ALL_REVIEWS_QUERY = "SELECT * FROM REVIEW ORDER BY r_date DESC, r_time DESC";
	private static final String SELECT_REVIEW_BY_USER_ID_QUERY = "SELECT * FROM REVIEW WHERE r_user_id=? ORDER BY r_date DESC";
	private static final String SELECT_REVIEW_BY_FILM_ID_QUERY = "SELECT * FROM REVIEW WHERE r_film_id=? ORDER BY r_date DESC";
	private static final String DELETE_REVIEW_QUERY = "DELETE FROM REVIEW WHERE r_id=?";
	private static final String UPDATE_FILM_RATING_QUERY = "UPDATE FILM SET f_rating = (SELECT AVG(r_mark) FROM review WHERE r_film_id = ?) WHERE film.f_id = ?";
	private static volatile ReviewDAO INSTANCE = null;
	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByUserID;
	private PreparedStatement psFindByFilmID;
	private PreparedStatement psFindAll;
	private PreparedStatement psDelete;
	private PreparedStatement psUpdateRating;

	private ReviewDAOImpl() {
	}

	public static ReviewDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new ReviewDAOImpl();
				instanceCreated.set(true);
			}
			lock.unlock();
		}
		return INSTANCE;
	}

	private static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	@Override
	public Review save(Review review) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_REVIEW_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setInt(1, review.getUserId());
			psSave.setInt(2, review.getFilmId());
			psSave.setDate(3, toSQLDate(review.getDate()));
			psSave.setInt(4, review.getMark());
			psSave.setString(5, review.getText());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				review.setId(rs.getInt(1));
			}
			return review;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL save problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}@Override
	public Review updateFilmRating(Review review) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_REVIEW_QUERY, Statement.RETURN_GENERATED_KEYS);
			psUpdateRating = con.prepareStatement(UPDATE_FILM_RATING_QUERY);
			psUpdateRating.setInt(1, review.getFilmId());
			psUpdateRating.setInt(2, review.getFilmId());
			psUpdateRating.executeUpdate();
			return review;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL save problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	@Override
	public Review find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_REVIEW_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateReview(rs);
			}
			return null;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL get problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	@Override
	public void update(Review review) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_REVIEW_QUERY);
			psUpdate.setInt(1, review.getUserId());
			psUpdate.setInt(2, review.getFilmId());
			psUpdate.setDate(3, toSQLDate(review.getDate()));
			psUpdate.setInt(4, review.getMark());
			psUpdate.setString(5, review.getText());
			psUpdate.setInt(6, review.getId());
			psUpdate.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}

	}

	@Override
	public int delete(Serializable id) throws DAOException {
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			psDelete = con.prepareStatement(DELETE_REVIEW_QUERY);
			psDelete.setInt(1, (int) id);
			return psDelete.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL delete problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				ConnectionManager.closeConnection();
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);
			}
		}
	}

	@Override
	public List<Review> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_REVIEWS_QUERY);
			List<Review> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
			}
			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	@Override
	public List<Review> findByUser(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByUserID = con.prepareStatement(SELECT_REVIEW_BY_USER_ID_QUERY);
			List<Review> list = new ArrayList<>();
			psFindByUserID.setInt(1, user.getId());
			psFindByUserID.execute();
			rs = psFindByUserID.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
			}
			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	@Override
	public List<Review> findByFilm(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByFilmID = con.prepareStatement(SELECT_REVIEW_BY_FILM_ID_QUERY);
			List<Review> list = new ArrayList<>();
			psFindByFilmID.setInt(1, film.getId());
			psFindByFilmID.execute();
			rs = psFindByFilmID.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
			}
			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				ConnectionManager.closeConnection();

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	private Review populateReview(ResultSet rs) throws SQLException {
		Review review = new Review();
		review.setId(rs.getInt(1));
		review.setUserId(rs.getInt(2));
		review.setFilmId(rs.getInt(3));
		review.setDate(toLocalDate(rs.getDate(4)));
		review.setMark(rs.getInt(5));
		review.setText(rs.getString(6));
		return review;
	}
}
