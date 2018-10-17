package com.epam.training.web.command.impl.navbar;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.User;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.web.command.Command;

import lombok.extern.log4j.Log4j;

@Log4j
public class AddBalanceCommand implements Command {
	private UserService userService = UserServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		try {
			if (req.getSession().getAttribute("suser") == null) {
				String contextPath = req.getContextPath();
				resp.sendRedirect(contextPath + "/frontController?command=error");
			}
			User user = (User) req.getSession().getAttribute("suser");
			user = userService.addBalance(user);
			req.getSession().setAttribute("suser", user);
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath + "/frontController?command=main");
		} catch (NumberFormatException e) {
			log.error(e);
			throw new ServiceException("Number format", e);
		}

	}

}
