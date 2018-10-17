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

import com.epam.training.dao.DiscountDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.ConnectionManager;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Discount;
import com.epam.training.entity.User;
import com.epam.training.util.SqlDateConverter;

import lombok.extern.log4j.Log4j;

@Log4j
public class DiscountDAOImpl extends SqlDateConverter implements DiscountDAO {
	private static final String SAVE_DISCOUNT_QUERY = "INSERT INTO discounts (d_user, d_amount, d_stdate, d_endate) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_DISCOUNT_QUERY = "UPDATE discounts SET d_amount = ?, d_endate = ? WHERE d_id = ?";
	private static final String SELECT_DISCOUNT_QUERY = "SELECT * FROM DISCOUNT WHERE d_id=?";
	private static final String SELECT_ALL_DISCOUNTS_QUERY = "SELECT * FROM DISCOUNT";
	private static final String SELECT_DISCOUNT_BY_USER_ID_QUERY = "SELECT * FROM DISCOUNT WHERE d_user_id=?";
	private static final String DELETE_DISCOUNT_QUERY = "DELETE FROM DISCOUNT WHERE d_id=?";
	private static DiscountDAO INSTANCE;
	private static Lock lock = new ReentrantLock();
	private static AtomicBoolean instanceCreated = new AtomicBoolean();
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psFind;
	private PreparedStatement psFindByUserID;
	private PreparedStatement psFindAll;
	private PreparedStatement psDelete;

	private DiscountDAOImpl() {
	}

	public static DiscountDAO getInstance() {
		if (!instanceCreated.get()) {
			lock.lock();
			if (INSTANCE == null) {
				INSTANCE = new DiscountDAOImpl();
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
	public Discount save(Discount discount) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psSave = con.prepareStatement(SAVE_DISCOUNT_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setInt(1, discount.getUserId());
			psSave.setInt(2, discount.getAmount());
			psSave.setDate(3, toSQLDate(discount.getStDate()));
			psSave.setDate(3, toSQLDate(discount.getEnDate()));
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				discount.setId(rs.getInt(1));
			}
			return discount;
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
	public Discount find(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFind = con.prepareStatement(SELECT_DISCOUNT_QUERY);
			psFind.setInt(1, (int) id);
			psFind.executeQuery();
			rs = psFind.getResultSet();
			if (rs.next()) {
				return populateDiscount(rs);
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
	public void update(Discount discount) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psUpdate = con.prepareStatement(UPDATE_DISCOUNT_QUERY);
			psSave.setInt(1, discount.getAmount());
			psSave.setDate(2, toSQLDate(discount.getEnDate()));
			psUpdate.setInt(3, discount.getId());
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
			psDelete = con.prepareStatement(DELETE_DISCOUNT_QUERY);
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
	public List<Discount> findAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindAll = con.prepareStatement(SELECT_ALL_DISCOUNTS_QUERY);
			List<Discount> list = new ArrayList<>();
			psFindAll.execute();
			rs = psFindAll.getResultSet();
			while (rs.next()) {
				list.add(populateDiscount(rs));
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
	public Discount findByUser(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			psFindByUserID = con.prepareStatement(SELECT_DISCOUNT_BY_USER_ID_QUERY);
			psFindByUserID.setInt(1, user.getId());
			psFindByUserID.execute();
			rs = psFindByUserID.getResultSet();
			while (rs.next()) {
				return populateDiscount(rs);
			}
			return null;
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

	private Discount populateDiscount(ResultSet rs) throws SQLException {
		Discount discount = new Discount();
		discount.setId(rs.getInt(1));
		discount.setUserId(rs.getInt(2));
		discount.setAmount(rs.getInt(3));
		discount.setStDate(toLocalDate(rs.getDate(4)));
		discount.setEnDate(toLocalDate(rs.getDate(4)));
		return discount;
	}
}
