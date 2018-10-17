package com.epam.training.web.command.impl.navbar;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.service.FilmService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.web.command.Command;

public class CatalogCommand implements Command {
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ServiceException {
		int totalPageAmount = filmService.getNumberOfAllFilmsPages();
		int pageNum;
		if (req.getParameter("page") == null) {
			pageNum = 1;
		} else {
			pageNum = Integer.parseInt(req.getParameter("page"));
		}
		List<Film> films = filmService.findAllByPage(pageNum);
		req.getSession().setAttribute("totalPageAmount", totalPageAmount);
		req.getSession().setAttribute("pageNum", pageNum);
		req.getSession().setAttribute("Msg", "");
		req.getSession().setAttribute("films", films);
		req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
	}
}
