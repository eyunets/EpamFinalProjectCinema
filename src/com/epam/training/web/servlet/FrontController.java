package com.epam.training.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.db.ConnectionPool;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.web.command.enums.CommandType;
import com.epam.training.web.handler.RequestHandler;

import lombok.extern.log4j.Log4j;

@WebServlet(urlPatterns = "/frontController")
@Log4j
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = -4831892119057945503L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CommandType commandType = RequestHandler.getCommand(req);
		try {
			commandType.getCommand().execute(req, resp);
		} catch (ServiceException e) {
			log.error(e);
			req.getRequestDispatcher(req.getContextPath() + "/frontController?command=error").forward(req, resp);
		}
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ConnectionPool.getInstance();
		log.info("Server started.");
	}

	@Override
	public void destroy() {
		super.destroy();
		ConnectionPool.closePool();
		log.info("Server stopped.");
	}

}
