package com.epam.training.web.command.impl.navbar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.web.command.Command;

public class LogoutCommand implements Command {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		String contextPath = req.getContextPath();
		resp.sendRedirect(contextPath + "/frontController?command=main");
	}
}
