package com.epam.training.web.command.enums;

import com.epam.training.web.command.Command;
import com.epam.training.web.command.impl.*;
import com.epam.training.web.command.impl.film.AddFilmCommand;
import com.epam.training.web.command.impl.film.DeleteFilmCommand;
import com.epam.training.web.command.impl.film.EditFilmCommand;
import com.epam.training.web.command.impl.film.FilmCommand;
import com.epam.training.web.command.impl.film.UserFilmsCommand;
import com.epam.training.web.command.impl.film.ReserveFilmCommand;
import com.epam.training.web.command.impl.film.ReturnFilmCommand;
import com.epam.training.web.command.impl.navbar.AboutCommand;
import com.epam.training.web.command.impl.navbar.CatalogCommand;
import com.epam.training.web.command.impl.navbar.LoginCommand;
import com.epam.training.web.command.impl.navbar.LogoutCommand;
import com.epam.training.web.command.impl.navbar.MainCommand;
import com.epam.training.web.command.impl.navbar.SearchCatalogCommand;
import com.epam.training.web.command.impl.navbar.SignUpCommand;
import com.epam.training.web.command.impl.user.BanUserCommand;
import com.epam.training.web.command.impl.user.EditUserCommand;
import com.epam.training.web.command.impl.user.UsersCommand;
import com.epam.training.web.command.impl.navbar.AddBalanceCommand;
import com.epam.training.web.command.impl.review.AddReviewCommand;
import com.epam.training.web.command.impl.review.DeleteReviewCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {

	MAIN("main/main.jsp", "main", new MainCommand()), SIGN_UP("login.jsp", "signup", new SignUpCommand()),
	LOGIN("login.jsp", "login", new LoginCommand()), ADD_FILM("admin/addFilm.jsp", "addfilm", new AddFilmCommand()),
	CATALOG("catalog/main.jsp", "catalog", new CatalogCommand()), FILM("catalog/film.jsp", "film", new FilmCommand()),
	ERROR("error/error.jsp", "error", new ErrorCommand()), LOGOUT("", "logout", new LogoutCommand()),
	NOT_FOUND("error/404.jsp", "404", new ErrorCommand()), ABOUT("about/main.jsp", "about", new AboutCommand()),
	USERS("admin/users.jsp", "users", new UsersCommand()),
	EDIT_USER("cabinet/editUser.jsp", "edituser", new EditUserCommand()),
	MY_FILMS("cabinet/myFilms.jsp", "myfilms", new UserFilmsCommand()),
	EDIT_FILM("admin/editFilm.jsp", "editFilm", new EditFilmCommand()),
	DELETE_FILM("catalog/film.jsp", "deleteFilm", new DeleteFilmCommand()),
	RESERVE_FILM_AJAX("", "reserveFilm", new ReserveFilmCommand()),
	RETURN_FILM_AJAX("", "returnFilm", new ReturnFilmCommand()), BAN_USER_AJAX("", "banUser", new BanUserCommand()),
	SEARCH_CATALOG("catalog/main.jsp", "searchcatalog", new SearchCatalogCommand()),
	ADD_REVIEW_COMMAND("catalog/addreview.jsp", "addreview", new AddReviewCommand()),
	DELETE_REVIEW_COMMAND("", "deleteReview", new DeleteReviewCommand()),
	ADD_BALANCE_COMMAND("", "addBalance", new AddBalanceCommand());

	private String pagePath;
	private String pageName;
	private Command command;

	/**
	 * @param page page name
	 * @return matching command type or MAIN by default
	 */
	public static CommandType getByPageName(String page) {
		for (CommandType type : CommandType.values()) {
			if (type.pageName.equalsIgnoreCase(page)) {
				return type;
			}
		}
		return MAIN;
	}
}
