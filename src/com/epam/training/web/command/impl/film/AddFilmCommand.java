package com.epam.training.web.command.impl.film;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.service.FilmService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.util.Validator;
import com.epam.training.web.command.Command;

public class AddFilmCommand implements Command {
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		if (req.getMethod().equals("GET")) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
		} else {
			Film film = new Film();
			if (Validator.isFilmValid(film, req)) {
				filmService.save(film);
				req.getSession().setAttribute("errorMsg", "");
				String contextPath = req.getContextPath();
				resp.sendRedirect(contextPath + "/frontController?command=catalog");
				return;
			} else { // forward user to the same page with error message
				req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
				RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
				dispatcher.forward(req, resp);
				return;
			}
		}
	}

}