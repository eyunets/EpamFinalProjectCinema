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

public class ReturnFilmCommand implements Command {
    private FilmService filmService = FilmServiceImpl.getInstance();
    private TicketService ticketService = TicketServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ServiceException {

        Film film = filmService.find(Integer.parseInt(req.getParameter("filmID")));
        Ticket ticket = null;
        if (req.getSession().getAttribute("suser") == null) {
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath + "/frontController?command=error");
        }
        int userID = ((User) req.getSession().getAttribute("suser")).getId();
        ArrayList<Ticket> tickets = new ArrayList<>(ticketService.findByFilm(film));
        for (Ticket t : tickets) {
            if (t.getUserId() == userID)
                ticket = t;
        }
        if (ticket == null) {
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath + "/frontController?command=error");
        } else {
            ticketService.delete(ticket.getId());
        }
    }
}
