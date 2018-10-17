package com.epam.training.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import com.epam.training.entity.Discount;
import com.epam.training.entity.Film;
import com.epam.training.entity.News;
import com.epam.training.entity.Review;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;
import com.epam.training.service.FilmService;
import com.epam.training.service.UserService;
import com.epam.training.service.exception.ServiceException;
import com.epam.training.service.impl.FilmServiceImpl;
import com.epam.training.service.impl.UserServiceImpl;

import lombok.extern.log4j.Log4j;

@Log4j
public class Validator {

	public static boolean isFilmValid(Film film, HttpServletRequest req) {
		boolean validData = true; // flag to indicate whether all input data is valid
		if (req.getParameter("name").matches("^.{1,29}$")) {
			film.setName(req.getParameter("name"));
		} else {
			validData = false;
		}
		if (req.getParameter("genre").matches("^.{1,30}$")) {
			film.setGenre(req.getParameter("genre"));
		} else {
			validData = false;
		}
		LocalDate year;
		try {
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			year = LocalDate.parse(req.getParameter("year"));
			film.setYear(year);
		} catch (NumberFormatException | DateTimeParseException e) {
			log.error(e);
			validData = false;
		}

		if (req.getParameter("description").matches("^.{1,255}$")) {
			film.setDescription(req.getParameter("description"));
		} else {
			validData = false;
		}

		float price;
		try {
			price = Float.parseFloat(req.getParameter("price"));
			if (price > 0) {
				film.setPrice(price);
			} else {
				validData = false;
			}
		} catch (NumberFormatException e) {
			log.error(e);
			validData = false;
		}
		return validData;
	}

	public static boolean isUserValid(User user, HttpServletRequest req) throws ServiceException {
		boolean validData = true;// flag to indicate whether all input data is valid
		if (req.getParameter("surname").matches("^[À-ß¨][a-ÿ¸]{0,29}$")) {
			user.setSurname(req.getParameter("surname"));
		} else {
			validData = false;
		}
		if (req.getParameter("name").matches("^[À-ß¨][a-ÿ¸]{0,29}$")) {
			user.setName(req.getParameter("name"));
		} else {
			validData = false;
		}
		if (req.getParameter("email").matches("^([a-z0-9_\\.-]+\\@[\\da-z\\.-]+\\.[a-z\\.]{2,6})$")) {
			UserService userService = UserServiceImpl.getInstance();
			User userEmail = userService.findByEmail(req.getParameter("email"));
			if (userEmail != null) {
				validData = false;
			}
			user.setEmail(req.getParameter("email"));
		} else {
			validData = false;
		}
		if (req.getParameter("password").matches(".{6,30}")) {
			user.setPassword(req.getParameter("password"));
		} else {
			validData = false;
		}
		return validData;
	}

	public static boolean isReviewValid(Review review, HttpServletRequest req) {
		boolean validData = true;// flag to indicate whether all input data is valid
		if (req.getParameter("mark").matches("^[1-5]$")) {
			review.setMark(Integer.parseInt(req.getParameter("mark")));
		} else {
			validData = false;
		}
		if (req.getParameter("text").matches("^.{1,255}$")) {
			review.setText(req.getParameter("text"));
		} else {
			validData = false;
		}
		return validData;
	}

	public static boolean isEnoughBalance(Film film, User user, Discount discount) {
		if (discount != null) {
			int result = user.getBalance()
					.compareTo(new BigDecimal(film.getPrice() * (100 - discount.getAmount()) / 100));
			if (result == -1)
				return false;
			else
				return true;
		} else {
			int result = user.getBalance().compareTo(new BigDecimal(film.getPrice()));
			if (result == -1)
				return false;
			else
				return true;
		}
	}

}