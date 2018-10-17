package com.epam.training.web.command.impl.navbar;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.User;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.util.PasswordEncoder;
import com.epam.training.web.command.Command;

public class LoginCommand implements Command {
	UserService userService = UserServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		if (login == null || password == null) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
			return;
		}
		User user = userService.findByEmail(login);
		if (user != null && user.getPassword().equals(PasswordEncoder.encode(password))) {
			// if (user != null && password.equals(Encoder.encode(user.getPassword()))) {
			if (user.getType().equals("admin")) {
				req.getSession().setAttribute("sadmin", user);
			} else {
				req.getSession().setAttribute("suser", user);
			}
			req.getSession().setAttribute("errorMsg", "");
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=main");
			return;
		} else {
			req.getSession().setAttribute("errorMsg", "Invalid Login or Password");
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
			return;
		}
	}
}
