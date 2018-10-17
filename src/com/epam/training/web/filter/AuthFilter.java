package com.epam.training.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.training.web.command.enums.CommandType;
import com.epam.training.web.handler.RequestHandler;
import com.google.gson.Gson;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "/frontController" })
public class AuthFilter implements Filter {
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();

		ArrayList<CommandType> userOnly = new ArrayList<>();
		userOnly.add(CommandType.EDIT_USER);
		userOnly.add(CommandType.MY_FILMS);
		userOnly.add(CommandType.RESERVE_FILM_AJAX);
		userOnly.add(CommandType.ADD_BALANCE_COMMAND);
		userOnly.add(CommandType.ADD_REVIEW_COMMAND);

		ArrayList<CommandType> adminOnly = new ArrayList<>();
		adminOnly.add(CommandType.ADD_FILM);
		adminOnly.add(CommandType.BAN_USER_AJAX);
		adminOnly.add(CommandType.EDIT_FILM);
		adminOnly.add(CommandType.DELETE_FILM);
		adminOnly.add(CommandType.USERS);

		ArrayList<CommandType> userAndAdmin = new ArrayList<>();
		userAndAdmin.add(CommandType.DELETE_REVIEW_COMMAND);
		CommandType type = RequestHandler.getCommand(request);
		System.out.println(type);
		if (userAndAdmin.contains(type)) {
			if ((session.getAttribute("suser") == null) && (session.getAttribute("sadmin") == null)) {
				String contextPath = request.getContextPath();
				response.sendRedirect(contextPath + "/frontController?command=login");
				return;
			}
		} else {
			if (userOnly.contains(type)) {
				if ((session.getAttribute("suser") == null)) {
					String contextPath = request.getContextPath();
					response.sendRedirect(contextPath + "/frontController?command=login");
					return;
				}
			} else if (adminOnly.contains(type)) {
				if ((session.getAttribute("sadmin") == null)) {
					String contextPath = request.getContextPath();
					response.sendRedirect(contextPath + "/frontController?command=login");
					return;
				}
			}
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig config) throws ServletException {

	}

}