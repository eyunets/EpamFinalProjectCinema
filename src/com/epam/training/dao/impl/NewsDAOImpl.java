package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.training.dao.NewsDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.News;
import com.epam.training.util.SqlDateConverter;

import lombok.extern.log4j.Log4j;

@Log4j
public class NewsDAOImpl extends SqlDateConverter implements NewsDAO {
	private static final String SAVE_NEWS_QUERY = "INSERT INTO NEWS (n_date, n_time, n_title, n_text) VALUES (?, ?, ?, ?)";
	private static final String DELETE_NEWS_QUERY = "DELETE FROM NEWS WHERE n_id = ?";
	private static final String UPDATE_NEWS_QUERY = "UPDATE NEWS SET n_title = ?, n_text = ? WHERE n_id = ?";
	private static final String SELECT_ALL_NEWS_QUERY = "SELECT * FROM NEWS TICKET BY n_date DESC, n_time DESC";
	private static final String SELECT_NEWS_QUERY = "SELECT * FROM NEWS WHERE n_id = ?";
	private static final String SELECT_NEWS_BY_YEAR = "SELECT * FROM NEWS WHERE YEAR(n_date) = ? TICKET BY n_date DESC, n_time DESC";
	private static NewsDAO INSTANCE;
	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByYear;
	private PreparedStatement psFindAll;
	private PreparedStatement psDelete;

	private NewsDAOImpl() {
	}

	public static NewsDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new NewsDAOImpl();
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
	public News save(News news) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_NEWS_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setDate(1, toSQLDate(news.getDate()));
			psSave.setTime(2, news.getTime());
			psSave.setString(3, news.getTitle());
			psSave.setString(4, news.getText());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				news.setId(rs.getInt(1));
			}
			return news;
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
	public News find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_NEWS_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateNews(rs);
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
	public void update(News news) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_NEWS_QUERY);
			psUpdate.setDate(1, toSQLDate(news.getDate()));
			psUpdate.setTime(2, news.getTime());
			psUpdate.setString(3, news.getTitle());
			psUpdate.setString(4, news.getText());
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
			psDelete = con.prepareStatement(DELETE_NEWS_QUERY);
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
	public List<News> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_NEWS_QUERY);
			List<News> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateNews(rs));
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
	public List<News> findNewsByYear(LocalDate year) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByYear = con.prepareStatement(SELECT_NEWS_BY_YEAR);
			psFindByYear.setDate(1, toSQLDate(year));
			List<News> list = new ArrayList<>();
			psFindByYear.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateNews(rs));
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

	private News populateNews(ResultSet rs) throws SQLException {
		News news = new News();
		news.setId(rs.getInt(1));
		news.setDate(toLocalDate(rs.getDate(2)));
		news.setTime(rs.getTime(3));
		news.setTitle(rs.getString(4));
		news.setText(rs.getString(5));
		return news;
	}
}
