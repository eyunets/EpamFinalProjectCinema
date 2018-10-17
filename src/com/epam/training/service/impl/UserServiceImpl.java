package com.epam.training.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.TicketDAO;
import com.epam.training.dao.UserDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.TicketDAOImpl;
import com.epam.training.dao.impl.UserDAOImpl;
import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;

public class UserServiceImpl extends TransactionManager implements UserService {
	private static volatile UserService INSTANCE = null;
	private UserDAO userDAO = UserDAOImpl.getInstance();
	private TicketDAO ticketDAO = TicketDAOImpl.getInstance();

	private UserServiceImpl() {
	}

	public static UserService getInstance() {
		UserService UserService = INSTANCE;
		if (UserService == null) {
			synchronized (UserServiceImpl.class) {
				UserService = INSTANCE;
				if (UserService == null) {
					INSTANCE = UserService = new UserServiceImpl();
				}
			}
		}

		return UserService;
	}

	@Override
	public User save(User user) throws ServiceException {

		try {
			if (user != null) {
				startTransaction();
				user = userDAO.save(user);
				commit();
				return user;
			} else {
				throw new ServiceException("User not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating User", e);
		}
	}

	@Override
	public User find(Serializable id) throws ServiceException {
		User user = new User();
		try {
			startTransaction();
			user = userDAO.find(id);
			commit();
			return user;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public void update(User user) throws ServiceException {
		try {
			startTransaction();
			userDAO.update(user);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating User", e);
		}
	}
	
	@Override
	public void update(User oldUser, User newUser) throws ServiceException {
		User user = new User();
		user.setId(oldUser.getId());
		user.setSurname((newUser.getSurname().length() == 0) ? oldUser.getSurname() : newUser.getSurname());
		user.setName((newUser.getName().length() == 0) ? oldUser.getName() : newUser.getName());
		user.setEmail((newUser.getEmail().length() == 0) ? oldUser.getEmail() : newUser.getEmail());
		user.setPassword((newUser.getPassword().length() == 0) ? oldUser.getPassword() : newUser.getPassword());
		user.setType(oldUser.getType());
		update(user);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			int rows = userDAO.delete(id);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting User", e);
		}
	}

	@Override
	public List<User> findAll() throws ServiceException {
		ArrayList<User> users;
		try {
			startTransaction();
			users = new ArrayList<>(userDAO.findAll());
			commit();
			return users;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public User findByEmail(String email) throws ServiceException {
		ArrayList<User> users;
		try {
			startTransaction();
			users = new ArrayList<>(userDAO.findByEmail(email));
			if (users.size() > 1)
				throw new ServiceException("Multiple login Error");
			commit();
			if (users.isEmpty())
				return null;
			return users.get(0);
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public List<User> findByType(String type) throws ServiceException {
		ArrayList<User> users;
		try {
			startTransaction();
			users = new ArrayList<>(userDAO.findByType(type));
			commit();
			return users;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public List<User> findByFilm(Film film) throws ServiceException {
		ArrayList<User> users = new ArrayList<>();
		ArrayList<Ticket> tickets;
		try {
			startTransaction();
			tickets = new ArrayList<>(ticketDAO.findByFilm(film));
			for(int i = 0 ; i < tickets.size(); i++) {
				users.add(userDAO.find(tickets.get(i).getUserId()));
			}
			commit();
			return users;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
		
	}

	@Override
	public User addBalance(User user) throws ServiceException {
		try {
			startTransaction();
			user = userDAO.find(user.getId());
			user = userDAO.addBalance(user);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error adding balance", e);
		}
		return user;
	}

}
