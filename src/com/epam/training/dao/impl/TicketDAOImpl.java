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

import com.epam.training.dao.TicketDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.util.SqlDateConverter;

import lombok.extern.log4j.Log4j;

@Log4j
public class TicketDAOImpl extends SqlDateConverter implements TicketDAO {
	private static final String SAVE_TICKET_QUERY = "INSERT INTO TICKET (t_user_id, t_film_id, t_date, t_price, t_discount, t_payment) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_TICKET_QUERY = "UPDATE TICKET SET t_user_id=?, t_film_id=?, t_date=? t_price = ?, t_discount = ?, t_payment = ? WHERE t_id=?";
	private static final String SELECT_TICKET_QUERY = "SELECT * FROM TICKET WHERE t_id=?";
	private static final String SELECT_ALL_TICKETS_QUERY = "SELECT * FROM TICKET";
	private static final String SELECT_TICKET_BY_USER_ID_QUERY = "SELECT * FROM TICKET WHERE t_user_id=?";
	private static final String SELECT_TICKET_BY_FILM_ID_QUERY = "SELECT * FROM TICKET WHERE t_film_id=?";
	private static final String DELETE_TICKET_QUERY = "DELETE FROM TICKET WHERE t_id=?";
	private static TicketDAO INSTANCE;
	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByUserID;
	private PreparedStatement psFindByFilmID;
	private PreparedStatement psFindAll;
	private PreparedStatement psDelete;

	private TicketDAOImpl() {
	}

	public static TicketDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new TicketDAOImpl();
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
	public Ticket save(Ticket ticket) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_TICKET_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setInt(1, ticket.getUserId());
			psSave.setInt(2, ticket.getFilmId());
			psSave.setDate(3, toSQLDate(ticket.getDate()));
			psSave.setFloat(4, ticket.getPrice());
			psSave.setInt(5, ticket.getDiscount());
			psSave.setFloat(6, ticket.getPayment());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				ticket.setId(rs.getInt(1));
			}
			return ticket;
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
	public Ticket find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_TICKET_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateTicket(rs);
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
	public void update(Ticket ticket) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_TICKET_QUERY);
			psSave.setInt(1, ticket.getUserId());
			psSave.setInt(2, ticket.getFilmId());
			psSave.setDate(3, toSQLDate(ticket.getDate()));
			psUpdate.setInt(4, ticket.getId());
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
			psDelete = con.prepareStatement(DELETE_TICKET_QUERY);
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
	public List<Ticket> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_TICKETS_QUERY);
			List<Ticket> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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
	public List<Ticket> findByUser(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByUserID = con.prepareStatement(SELECT_TICKET_BY_USER_ID_QUERY);
			List<Ticket> list = new ArrayList<>();
			psFindByUserID.setInt(1, user.getId());
			psFindByUserID.execute();
			rs = psFindByUserID.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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
	public List<Ticket> findByFilm(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByFilmID = con.prepareStatement(SELECT_TICKET_BY_FILM_ID_QUERY);
			List<Ticket> list = new ArrayList<>();
			psFindByFilmID.setInt(1, film.getId());
			psFindByFilmID.execute();
			rs = psFindByFilmID.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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

	private Ticket populateTicket(ResultSet rs) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setId(rs.getInt(1));
		ticket.setUserId(rs.getInt(2));
		ticket.setFilmId(rs.getInt(3));
		ticket.setDate(toLocalDate(rs.getDate(4)));
		return ticket;
	}
}
