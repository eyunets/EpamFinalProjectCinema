package com.epam.training.web.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.web.command.Command;

public class ErrorCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
        req.setAttribute("title", "error");
        dispatcher.forward(req, resp);
        return;
    }

}
