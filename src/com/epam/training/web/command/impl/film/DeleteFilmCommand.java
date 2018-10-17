package com.epam.training.web.command.impl.film;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.service.FilmService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.web.command.Command;

import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public class DeleteFilmCommand implements Command {
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		try {
			Film film = filmService.find(Integer.parseInt(req.getParameter("id")));
			if (film != null) {
				filmService.delete(film.getId());
			}
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=catalog");
		} catch (NumberFormatException e) {
			log.error(e);
			throw new ServiceException("Number format", e);
		}

	}
}
