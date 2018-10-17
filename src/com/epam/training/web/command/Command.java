package com.epam.training.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.service.exception.ServiceException;

public interface Command {
	String MAIN_PAGE = "/WEB-INF/view/layouts/default.jspx";

	void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ServiceException;
}