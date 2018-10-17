package com.epam.training.web.command.impl.navbar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.web.command.Command;

import java.io.IOException;

public class AboutCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getServletContext().getRequestDispatcher(MAIN_PAGE).forward(req, resp);
    }
}
