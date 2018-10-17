package com.epam.training.web.command.impl.film;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.service.FilmService;
import com.epam.training.service.TicketService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.TicketServiceImpl;
import com.epam.training.web.command.Command;

import java.io.IOException;
import java.util.ArrayList;

public class UserFilmsCommand implements Command {
	private TicketService ticketService = TicketServiceImpl.getInstance();
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ServiceException {
		if (req.getSession().getAttribute("suser") == null) {
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=error");
		}
		User user = (User) req.getSession().getAttribute("suser");
		ArrayList<Ticket> tickets = new ArrayList<>(ticketService.findByUser(user));
		ArrayList<Film> films = new ArrayList<>();
		for (Ticket t : tickets) {
			films.add(filmService.find(t.getFilmId()));
		}
		req.getSession().setAttribute("tickets", tickets);
		req.getSession().setAttribute("films", films);
		req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
	}
}
