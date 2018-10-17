package com.epam.training.web.command.impl.film;

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

import java.io.IOException;

public class EditFilmCommand implements Command {
    private FilmService filmService = FilmServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ServiceException {
        if (req.getMethod().equals("GET")) {
            int filmID = Integer.parseInt(req.getParameter("id"));
            Film film = filmService.find(filmID);
            req.getSession().setAttribute("film", film);
            RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
            dispatcher.forward(req, resp);
            return;
        } else {
            int filmID = Integer.parseInt(req.getParameter("id"));
            Film oldFilm = filmService.find(filmID);
            Film newFilm = new Film();
            if (Validator.isFilmValid(newFilm, req)) {
                filmService.update(oldFilm, newFilm);
                req.getSession().setAttribute("errorMsg", "");
                String contextPath = req.getContextPath();
                resp.sendRedirect(contextPath + "/frontController?command=catalog");
                return;
            } else { //forward user to the same page with error message
                req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
                RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
                dispatcher.forward(req, resp);
                return;
            }
        }
    }
}
