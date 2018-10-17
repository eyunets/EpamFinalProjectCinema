package com.epam.training.web.command.impl.user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.User;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.web.command.Command;

import java.io.IOException;
import java.util.ArrayList;


public class UsersCommand implements Command {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ServiceException {
        ArrayList<User> users = new ArrayList<>(userService.findByType("user"));
        users.addAll(new ArrayList<>(userService.findByType("banned")));
        req.getSession().setAttribute("users", users);
        req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
    }
}
