package com.epam.training.web.command.impl.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Review;
import com.epam.training.service.ReviewService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.ReviewServiceImpl;
import com.epam.training.web.command.Command;

public class OpenAllReviews implements Command {
	private ReviewService reviewService = ReviewServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ServiceException {
		List<Review> reviews = new ArrayList<>(reviewService.findAll());
		req.getSession().setAttribute("Msg", "");
		req.getSession().setAttribute("reviews", reviews);
		req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
	}
}