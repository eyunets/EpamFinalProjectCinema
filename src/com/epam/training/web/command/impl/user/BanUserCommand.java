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


public class BanUserCommand implements Command {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ServiceException {
        User user = userService.find(Integer.parseInt(req.getParameter("userID")));
        if (user.getType().equals("banned")) {
            user.setType("user");
        } else {
            user.setType("banned");
        }
        userService.update(user);
    }
}
