package com.epam.training.web.command.impl.film;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;
import com.epam.training.service.FilmService;
import com.epam.training.service.ReviewService;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.ReviewServiceImpl;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.web.command.Command;

public class FilmCommand implements Command {

	private FilmService filmService = FilmServiceImpl.getInstance();
	private UserService userService = UserServiceImpl.getInstance();
	private ReviewService reviewService = ReviewServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		int filmID = Integer.parseInt(req.getParameter("id"));
		Film film = filmService.find(filmID);
		List<User> users = new ArrayList<>(userService.findByFilm(film));
		List<Review> reviews = new ArrayList<>(reviewService.findByFilm(film));
		
		List<String> reviewLogins = new ArrayList<String>();
		for (Review r : reviews) {
			reviewLogins.add(userService.find(r.getUserId()).getName());
		}
		req.setAttribute("reviewLogins", reviewLogins);
		req.getSession().setAttribute("users", users);
		req.getSession().setAttribute("film", film);
		req.getSession().setAttribute("reviews", reviews);
		req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
	}
}
