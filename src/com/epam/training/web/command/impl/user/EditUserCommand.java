package com.epam.training.web.command.impl.user;

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

import java.io.IOException;

public class EditUserCommand implements Command {
	private UserService userService = UserServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ServiceException {
		if (req.getMethod().equals("GET")) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
			return;
		} else {
			User sessionUser = (User) req.getSession().getAttribute("suser");
			User newUser = new User();
			if (Validator.isUserValid(newUser, req)) {
				userService.update(sessionUser, newUser);
				req.getSession().setAttribute("errorMsg", "");
				req.getSession().setAttribute("suser", userService.find(sessionUser.getId()));
				String contextPath = req.getContextPath();
				resp.sendRedirect(contextPath + "/frontController?command=main");
				return;
			} else { // forward user to the same page with error message
				req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
				RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
				dispatcher.forward(req, resp);
				return;
			}

		}
	}
}
