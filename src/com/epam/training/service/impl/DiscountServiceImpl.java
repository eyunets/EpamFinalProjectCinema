package com.epam.training.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.DiscountDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.DiscountDAOImpl;
import com.epam.training.entity.Discount;
import com.epam.training.entity.User;
import com.epam.training.service.DiscountService;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.exception.ServiceException;

public class DiscountServiceImpl extends TransactionManager implements DiscountService {
	private static volatile DiscountService INSTANCE = null;
	private DiscountDAO discountDAO = DiscountDAOImpl.getInstance();

	private DiscountServiceImpl() {
	}

	public static DiscountService getInstance() {
		DiscountService DiscountService = INSTANCE;
		if (DiscountService == null) {
			synchronized (DiscountServiceImpl.class) {
				DiscountService = INSTANCE;
				if (DiscountService == null) {
					INSTANCE = DiscountService = new DiscountServiceImpl();
				}
			}
		}

		return DiscountService;
	}

	@Override
	public Discount save(Discount discount) throws ServiceException {

		try {
			if (discount != null) {
				startTransaction();
				discount = discountDAO.save(discount);
				commit();
				return discount;
			} else {
				throw new ServiceException("Discount not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating Discount", e);
		}
	}

	@Override
	public Discount find(Serializable id) throws ServiceException {
		Discount discount = new Discount();
		try {
			startTransaction();
			discount = discountDAO.find(id);
			commit();
			return discount;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public void update(Discount discount) throws ServiceException {
		try {
			startTransaction();
			discountDAO.update(discount);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating Discount", e);
		}
	}

	public void update(Discount oldDiscount, Discount newDiscount) throws ServiceException {
		Discount discount = new Discount();
		discount.setId(oldDiscount.getId());
		discount.setUserId(oldDiscount.getUserId());
		discount.setStDate((newDiscount.getStDate() == null) ? oldDiscount.getStDate() : newDiscount.getStDate());
		discount.setEnDate((newDiscount.getEnDate() == null) ? oldDiscount.getEnDate() : newDiscount.getEnDate());
		discount.setAmount((newDiscount.getAmount() == 0) ? oldDiscount.getAmount() : newDiscount.getAmount());
		update(discount);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			int rows = discountDAO.delete(id);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting Discount", e);
		}
	}

	@Override
	public List<Discount> findAll() throws ServiceException {
		ArrayList<Discount> discounts;
		try {
			startTransaction();
			discounts = new ArrayList<>(discountDAO.findAll());
			commit();
			return discounts;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Discount", e);
		}
	}

	@Override
	public Discount findByUser(User user) throws ServiceException {
		Discount discount;
		try {
			startTransaction();
			discount = discountDAO.findByUser(user);
			commit();
			return discount;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Discount", e);
		}
	}

}
