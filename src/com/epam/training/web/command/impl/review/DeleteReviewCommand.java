package com.epam.training.web.command.impl.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.service.FilmService;
import com.epam.training.service.ReviewService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.ReviewServiceImpl;
import com.epam.training.web.command.Command;

import lombok.extern.log4j.Log4j;

@Log4j
public class DeleteReviewCommand implements Command {
	private ReviewService reviewService = ReviewServiceImpl.getInstance();
	private FilmService filmService = FilmServiceImpl.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, ServiceException {
		if (req.getMethod().equals("GET")) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
		} else {
			try {
				Review review = reviewService.find(Integer.parseInt(req.getParameter("id")));
				Film film = filmService.find(review.getFilmId());
				if (review != null) {
					reviewService.delete(review.getId());
				}
				List<Review> reviews = new ArrayList<>(reviewService.findByFilm(film));
				req.getSession().setAttribute("reviews", reviews);
				String contextPath = req.getContextPath();
				resp.sendRedirect(contextPath + "/frontController?command=film&id=" + review.getFilmId());
			} catch (NumberFormatException | ServiceException e) {
				log.error(e);
				throw new ServiceException("Number format", e);
			}
		}
	}

}
