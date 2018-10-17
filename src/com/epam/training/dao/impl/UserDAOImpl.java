package com.epam.training.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.epam.training.dao.UserDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.util.PasswordEncoder;

import lombok.extern.log4j.Log4j;

@Log4j
public class UserDAOImpl implements UserDAO {

	private static final String SAVE_USER_QUERY = "INSERT INTO USER ( u_name, u_surname, u_password, u_type, u_email, u_balance) VALUES (?, ?, ?, ?, ?,?)";
	private static final String UPDATE_USER_QUERY = "UPDATE USER SET u_name=?, u_surname=?, u_password=?, u_type=?, u_email=? WHERE u_id=?";
	private static final String UPDATE_BALANCE_QUERY = "UPDATE USER SET u_balance=? WHERE u_id=?";
	private static final String SELECT_USER_QUERY = "SELECT * FROM USER WHERE u_id=?";
	private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM USER";
	private static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM USER WHERE u_email=?";
	private static final String SELECT_USER_BY_TYPE_QUERY = "SELECT * FROM USER WHERE u_type=?";
	private static final String DELETE_USER_QUERY = "DELETE FROM USER WHERE u_id=?";

	private static UserDAO INSTANCE;
	private static final BigDecimal START_BALANCE = new BigDecimal(0);
	private static final BigDecimal ADD_BALANCE = new BigDecimal(10);

	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByEmail;
	private PreparedStatement psFindByType;
	private PreparedStatement psFindAll;
	private PreparedStatement psDelete;

	private UserDAOImpl() {
	}

	public static UserDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new UserDAOImpl();
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
	public User save(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setString(1, user.getName());
			psSave.setString(2, user.getSurname());
			psSave.setString(3, PasswordEncoder.encode(user.getPassword()));
			psSave.setString(4, user.getType());
			psSave.setString(5, user.getEmail());
			psSave.setBigDecimal(6, START_BALANCE);
			psSave.executeUpdate();

			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				user.setId(rs.getInt(1));
			}
			return user;
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
	public User find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_USER_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateUser(rs);
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
	public void update(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_USER_QUERY);
			psUpdate.setString(1, user.getName());
			psUpdate.setString(2, user.getSurname());
			psUpdate.setString(3, user.getPassword());
			psUpdate.setString(4, user.getType());
			psUpdate.setString(5, user.getEmail());
			psUpdate.setInt(6, user.getId());
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
			psDelete = con.prepareStatement(DELETE_USER_QUERY);
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
	public List<User> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_USERS_QUERY);
			List<User> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
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
	public List<User> findByEmail(String email) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByEmail = con.prepareStatement(SELECT_USER_BY_EMAIL_QUERY);
			List<User> list = new ArrayList<>();
			psFindByEmail.setString(1, email);
			psFindByEmail.execute();
			rs = psFindByEmail.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
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
	public List<User> findByType(String type) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByType = con.prepareStatement(SELECT_USER_BY_TYPE_QUERY);
			List<User> list = new ArrayList<>();
			psFindByType.setString(1, type);
			psFindByType.execute();
			rs = psFindByType.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
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

	private User populateUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt(1));
		user.setName(rs.getString(2));
		user.setSurname(rs.getString(3));
		user.setPassword(rs.getString(4));
		user.setType(rs.getString(5));
		user.setEmail(rs.getString(6));
		user.setBalance(rs.getBigDecimal(7));
		return user;
	}

	@Override
	public User addBalance(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_BALANCE_QUERY);
			user.setBalance(user.getBalance().add(ADD_BALANCE));
			psUpdate.setBigDecimal(1, user.getBalance());
			psUpdate.setInt(2, user.getId());
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
		return user;
	}

	@Override
	public User removeBalance(User user, Ticket ticket) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_BALANCE_QUERY);
			user.setBalance(user.getBalance().subtract(new BigDecimal(ticket.getPayment())));
			psUpdate.setBigDecimal(1, user.getBalance());
			psUpdate.setInt(2, user.getId());
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
		return user;
	}
}
