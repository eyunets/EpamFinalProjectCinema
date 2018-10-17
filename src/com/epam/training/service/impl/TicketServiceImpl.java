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
import com.epam.training.service.TicketService;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.exception.ServiceException;

public class TicketServiceImpl extends TransactionManager implements TicketService {
	private static volatile TicketService INSTANCE = null;
	private TicketDAO ticketDAO = TicketDAOImpl.getInstance();
	private UserDAO userDAO = UserDAOImpl.getInstance();

	private TicketServiceImpl() {
	}

	public static TicketService getInstance() {
		TicketService TicketService = INSTANCE;
		if (TicketService == null) {
			synchronized (TicketServiceImpl.class) {
				TicketService = INSTANCE;
				if (TicketService == null) {
					INSTANCE = TicketService = new TicketServiceImpl();
				}
			}
		}

		return TicketService;
	}

	@Override
	public Ticket save(Ticket ticket) throws ServiceException {

		try {
			if (ticket != null) {
				startTransaction();
				ticket = ticketDAO.save(ticket);
				User user = userDAO.find(ticket.getUserId());
				userDAO.removeBalance(user,ticket);
				commit();
				return ticket;
			} else {
				throw new ServiceException("Ticket not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating Ticket", e);
		}
	}

	@Override
	public Ticket find(Serializable id) throws ServiceException {
		Ticket ticket = new Ticket();
		try {
			startTransaction();
			ticket = ticketDAO.find(id);
			commit();
			return ticket;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Ticket", e);
		}
	}

	@Override
	public void update(Ticket ticket) throws ServiceException {
		try {
			startTransaction();
			ticketDAO.update(ticket);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating Ticket", e);
		}
	}

	public void update(Ticket oldTicket, Ticket newTicket) throws ServiceException {
		Ticket ticket = new Ticket();
		ticket.setId(oldTicket.getId());
		ticket.setUserId(oldTicket.getUserId());
		ticket.setFilmId(oldTicket.getFilmId());
		ticket.setDate((newTicket.getDate() == null) ? oldTicket.getDate() : newTicket.getDate());
		update(ticket);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			int rows = ticketDAO.delete(id);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting Ticket", e);
		}
	}

	@Override
	public List<Ticket> findAll() throws ServiceException {
		ArrayList<Ticket> tickets;
		try {
			startTransaction();
			tickets = new ArrayList<>(ticketDAO.findAll());
			commit();
			return tickets;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Ticket", e);
		}
	}

	@Override
	public List<Ticket> findByFilm(Film film) throws ServiceException {
		ArrayList<Ticket> tickets;
		try {
			startTransaction();
			tickets = new ArrayList<>(ticketDAO.findByFilm(film));
			commit();
			return tickets;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Ticket", e);
		}
	}

	@Override
	public List<Ticket> findByUser(User user) throws ServiceException {
		ArrayList<Ticket> tickets;
		try {
			startTransaction();
			tickets = new ArrayList<>(ticketDAO.findByUser(user));
			commit();
			return tickets;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Ticket", e);
		}
	}

}
