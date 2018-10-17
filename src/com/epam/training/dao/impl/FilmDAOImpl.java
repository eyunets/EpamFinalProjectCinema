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

import com.epam.training.dao.FilmDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;
import com.epam.training.util.SqlDateConverter;

import lombok.extern.log4j.Log4j;

@Log4j
public class FilmDAOImpl extends SqlDateConverter implements FilmDAO {

	private static final String SAVE_FILM_QUERY = "INSERT INTO FILM (f_name, f_genre, f_year, f_description, f_rating, f_price) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_FILM_QUERY = "UPDATE FILM SET f_name=?, f_genre=?, f_year=? ,f_description=?, f_rating=?, f_price=? WHERE f_id=?";
	private static final String SELECT_FILM_QUERY = "SELECT * FROM FILM WHERE f_id=?";
	private static final String SELECT_ALL_FILM_QUERY = "SELECT * FROM FILM ORDER BY f_rating DESC";
	private static final String SELECT_ALL_FILM_BY_PAGE_QUERY = "SELECT * FROM FILM ORDER BY f_rating DESC LIMIT ?, ?";
	private static final String SELECT_FILM_BY_NAME_QUERY = "SELECT * FROM FILM WHERE f_name=?";
	private static final String SELECT_ALL_FILMS_COUNT_QUERY = "SELECT COUNT(*) FROM FILM";
	// private static final String getFilmByYearQuery = "SELECT * FROM FILM WHERE
	// YEAR=?";
	private static final String SELECT_FILM_BY_GENRE_QUERY = "SELECT * FROM FILM WHERE f_genre=?";
	private static final String DELETE_FILM_QUERY = "DELETE FROM FILM WHERE f_id=?";
	private static FilmDAO INSTANCE;
	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByName;
	// private PreparedStatement psFindByYear;
	private PreparedStatement psFindByGenre;
	private PreparedStatement psFindAll;
	private PreparedStatement psFindAllByPage;
	private PreparedStatement psDelete;
	private PreparedStatement psFilmCount;

	private FilmDAOImpl() {
	}

	public static FilmDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new FilmDAOImpl();
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

	private static void close(PreparedStatement ps) throws SQLException {
		if (ps != null)
			ps.close();
	}

	@Override
	public Film save(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_FILM_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setString(1, film.getName());
			psSave.setString(2, film.getGenre());
			psSave.setDate(3, toSQLDate(film.getYear()));
			psSave.setString(4, film.getDescription());
			psSave.setFloat(5, film.getRating());
			psSave.setFloat(6, film.getPrice());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				film.setId(rs.getInt(1));
			}
			return film;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL save problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection con problem", e);
		} finally {
			try {
				ConnectionManager.closeConnection();
				close(rs);
				close(psSave);
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
	public Film find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_FILM_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateFilm(rs);
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
				ConnectionManager.closeConnection();
				close(rs);
				close(psFind);
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
	public void update(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_FILM_QUERY);
			psUpdate.setString(1, film.getName());
			psUpdate.setString(2, film.getGenre());
			psUpdate.setDate(3, toSQLDate(film.getYear()));
			psUpdate.setString(4, film.getDescription());
			psUpdate.setFloat(5, film.getRating());
			psUpdate.setFloat(6, film.getPrice());
			psUpdate.setInt(7, film.getId());
			psUpdate.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				ConnectionManager.closeConnection();
				close(rs);
				close(psUpdate);
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
			psDelete = con.prepareStatement(DELETE_FILM_QUERY);
			psDelete.setInt(1, (int) id);
			return psDelete.executeUpdate();

		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("Failure during SQL Delete Request execution", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Failure during taking connection from ConnectionPool", e);
		} finally {
			try {
				ConnectionManager.closeConnection();
				close(psDelete);
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
	public List<Film> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_FILM_QUERY);
			List<Film> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
				ConnectionManager.closeConnection();
				close(psFindAll);
				close(rs);
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
	public List<Film> findByName(String name) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByName = con.prepareStatement(SELECT_FILM_BY_NAME_QUERY);
			List<Film> list = new ArrayList<>();
			psFindByName.setString(1, name);
			psFindByName.execute();
			rs = psFindByName.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
				ConnectionManager.closeConnection();
				close(rs);
				close(psFindByName);
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
	public List<Film> findByGenre(String genre) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByGenre = con.prepareStatement(SELECT_FILM_BY_GENRE_QUERY);
			List<Film> list = new ArrayList<>();
			psFindByGenre.setString(1, genre);
			psFindByGenre.execute();
			rs = psFindByGenre.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
				ConnectionManager.closeConnection();
				close(rs);
				close(psFindByGenre);
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
	public int getNumberOfFilms() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFilmCount = con.prepareStatement(SELECT_ALL_FILMS_COUNT_QUERY);
			rs = psFilmCount.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL Select exception", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				ConnectionManager.closeConnection();
				close(rs);
				close(psFindByGenre);
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);
			}
		}
		return 0;
	}

	@Override
	public List<Film> findAllByPage(int start, int amount) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAllByPage = con.prepareStatement(SELECT_ALL_FILM_BY_PAGE_QUERY);
			List<Film> list = new ArrayList<>();
			psFindAllByPage.setInt(1, start);
			psFindAllByPage.setInt(2, amount);
			psFindAllByPage.execute();
			rs = psFindAllByPage.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
				ConnectionManager.closeConnection();
				close(psFindAll);
				close(rs);
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	private Film populateFilm(ResultSet rs) throws SQLException {
		Film film = new Film();
		film.setId(rs.getInt(1));
		film.setName(rs.getString(2));
		film.setGenre(rs.getString(3));
		film.setYear(toLocalDate(rs.getDate(4)));
		film.setDescription(rs.getString(5));
		film.setRating(rs.getFloat(6));
		film.setPrice(rs.getFloat(7));
		return film;
	}

}
