package com.epam.training.web.command.impl.navbar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.service.FilmService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.web.command.Command;

import java.io.IOException;
import java.util.ArrayList;

public class SearchCatalogCommand implements Command {
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ServiceException {

		String name = req.getParameter("name");
		req.getSession().setAttribute("pageNum", null);
		if (name == null ||name.length() < 3 || name.length() > 30) {
			req.getSession().setAttribute("films", null);
			req.getSession().setAttribute("Msg", "Invalid input");
			//RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			String contextPath = req.getContextPath();
			 resp.sendRedirect(contextPath + "/frontController?command=catalog");
			//dispatcher.forward(req, resp);
			return;
		} else {
			ArrayList<Film> films = new ArrayList<>(filmService.findByName(name));
			if (films.isEmpty()) {
				req.getSession().setAttribute("films", null);
				req.getSession().setAttribute("Msg", "No films match your input");
				RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
				dispatcher.forward(req, resp);
				return;
			} else {
				req.getSession().setAttribute("films", films);
				req.getSession().setAttribute("Msg", "");
				//String contextPath = req.getContextPath();
				//resp.sendRedirect(contextPath + "/frontController?command=catalog");
				RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
				dispatcher.forward(req, resp);
				return;
			}

		}
	}
}
