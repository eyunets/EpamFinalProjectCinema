package com.epam.training.web.command.impl.film;

import com.epam.training.entity.Discount;
import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.service.DiscountService;
import com.epam.training.service.FilmService;
import com.epam.training.service.TicketService;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.DiscountServiceImpl;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.TicketServiceImpl;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.util.Validator;
import com.epam.training.web.command.Command;

import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Log4j
public class ReserveFilmCommand implements Command {
	private FilmService filmService = FilmServiceImpl.getInstance();
	private UserService userService = UserServiceImpl.getInstance();
	private DiscountService discountService = DiscountServiceImpl.getInstance();
	private TicketService ticketService = TicketServiceImpl.getInstance();
	private static final int PERCENTAGE = 100;

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ServiceException {
		if (req.getMethod().equals("GET")) {
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=catalog");
			return;
		} else {
			try {
				Film film = filmService.find(Integer.parseInt(req.getParameter("id")));
				User user = userService.find(((User) req.getSession().getAttribute("suser")).getId());
				Discount discount = discountService.findByUser(user);
				if (Validator.isEnoughBalance(film, user, discount)) {
					Ticket ticket = new Ticket();
					ticket.setFilmId(film.getId());
					ticket.setUserId(user.getId());
					ticket.setDate(LocalDate.now());
					ticket.setPrice(film.getPrice());
					ticket.setDiscount((discount == null) ? 0 : discount.getAmount());
					ticket.setPayment((discount == null) ? film.getPrice()
							: film.getPrice() * (PERCENTAGE - discount.getAmount()) / PERCENTAGE);
					ticketService.save(ticket);
					user = userService.find(((User) req.getSession().getAttribute("suser")).getId());
					req.getSession().setAttribute("suser", user);
					req.getSession().setAttribute("errorMsg", "");
					String contextPath = req.getContextPath();
					resp.sendRedirect(contextPath + "/frontController?command=film&id=" + film.getId());
					return;
				} else {
					req.getSession().setAttribute("errorMsg", "Not enough money");
					String contextPath = req.getContextPath();
					resp.sendRedirect(contextPath + "/frontController?command=film&id=" + film.getId());
					return;
				}
			} catch (NumberFormatException e) {
				log.error(e);
				throw new ServiceException("Number format", e);
			}
		}
	}
}
