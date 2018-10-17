package com.epam.training.service;

import java.util.List;

import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.service.exception.ServiceException;

public interface TicketService extends Service<Ticket> {
	List<Ticket> findByUser(User user) throws ServiceException;

	List<Ticket> findByFilm(Film film) throws ServiceException;

}
