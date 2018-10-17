package com.epam.training.web.command.impl.review;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;
import com.epam.training.service.FilmService;
import com.epam.training.service.ReviewService;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.ReviewServiceImpl;
import com.epam.training.service.impl.UserServiceImpl;
import com.epam.training.util.Validator;
import com.epam.training.web.command.Command;
public class AddReviewCommand implements Command {	
	
	private FilmService filmService = FilmServiceImpl.getInstance();
	private UserService userService = UserServiceImpl.getInstance();
	private ReviewService reviewService = ReviewServiceImpl.getInstance();
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ServiceException {
		if (req.getMethod().equals("GET")) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
			dispatcher.forward(req, resp);
		} else {
			Review review = new Review();
			if (Validator.isReviewValid(review, req)) {
				Film film = filmService.find(Integer.parseInt(req.getParameter("id")));
				User user = userService.find(((User) req.getSession().getAttribute("suser")).getId());
				review.setFilmId(film.getId());
				review.setUserId(user.getId());
				review.setDate(LocalDate.now());
				reviewService.save(review);
				req.getSession().setAttribute("errorMsg", "");
				String contextPath = req.getContextPath();
				resp.sendRedirect(contextPath + "/frontController?command=catalog");
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
