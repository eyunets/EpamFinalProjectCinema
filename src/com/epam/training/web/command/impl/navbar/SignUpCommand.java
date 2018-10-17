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
import com.epam.training.util.Validator;
import com.epam.training.web.command.Command;

public class SignUpCommand implements Command {
	private UserService userService = UserServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		User user = new User();
		if (Validator.isUserValid(user, req)) {
			user.setType("user");
			userService.save(user);
			req.getSession().setAttribute("errorMsg", "");
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=main");
			return;
		} else {
			req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
			return;
		}

	}
}
