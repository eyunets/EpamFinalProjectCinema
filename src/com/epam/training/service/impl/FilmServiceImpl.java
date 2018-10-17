package com.epam.training.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.FilmDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.FilmDAOImpl;
import com.epam.training.entity.Film;
import com.epam.training.service.FilmService;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.exception.ServiceException;

public class FilmServiceImpl extends TransactionManager implements FilmService {
	private static volatile FilmService INSTANCE = null;
	private static final int FILMS_ON_PAGE = 10;
	private FilmDAO filmDAO = FilmDAOImpl.getInstance();

	private FilmServiceImpl() {
	}

	public static FilmService getInstance() {
		FilmService FilmService = INSTANCE;
		if (FilmService == null) {
			synchronized (FilmServiceImpl.class) {
				FilmService = INSTANCE;
				if (FilmService == null) {
					INSTANCE = FilmService = new FilmServiceImpl();
				}
			}
		}

		return FilmService;
	}

	@Override
	public Film save(Film film) throws ServiceException {

		try {
			if (film != null) {
				startTransaction();
				film = filmDAO.save(film);
				commit();
				return film;
			} else {
				throw new ServiceException("Film not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating Film", e);
		}
	}

	@Override
	public Film find(Serializable id) throws ServiceException {
		Film film = new Film();
		try {
			startTransaction();
			film = filmDAO.find(id);
			commit();
			return film;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public void update(Film film) throws ServiceException {
		try {
			startTransaction();
			filmDAO.update(film);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating Film", e);
		}
	}

	public void update(Film oldFilm, Film newFilm) throws ServiceException {
		Film film = new Film();
		film.setId(oldFilm.getId());
		film.setName((newFilm.getName().length() == 0) ? oldFilm.getName() : newFilm.getName());
		film.setGenre((newFilm.getGenre().length() == 0) ? oldFilm.getGenre() : newFilm.getName());
		film.setYear((newFilm.getYear() == null) ? oldFilm.getYear() : newFilm.getYear());
		film.setDescription(
				(newFilm.getDescription().length() == 0) ? oldFilm.getDescription() : newFilm.getDescription());
		film.setPrice((newFilm.getPrice() == 0) ? oldFilm.getPrice() : newFilm.getPrice());
		film.setRating((newFilm.getRating() == 0) ? oldFilm.getRating() : newFilm.getRating());
		update(film);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			int rows = filmDAO.delete(id);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting Film", e);
		}
	}

	@Override
	public List<Film> findAll() throws ServiceException {
		ArrayList<Film> films;
		try {
			startTransaction();
			films = new ArrayList<>(filmDAO.findAll());
			commit();
			return films;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Film", e);
		}
	}

	@Override
	public List<Film> findAllByPage(int pageNum) throws ServiceException {
		ArrayList<Film> films;
		int start = (pageNum - 1) * FILMS_ON_PAGE;
		try {
			startTransaction();
			films = new ArrayList<>(filmDAO.findAllByPage(start, FILMS_ON_PAGE));
			commit();
			return films;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Film", e);
		}
	}

	@Override
	public List<Film> findByName(String name) throws ServiceException {
		ArrayList<Film> films;
		try {
			startTransaction();
			films = new ArrayList<>(filmDAO.findByName(name));
			commit();
			return films;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Film", e);
		}
	}

	@Override
	public List<Film> findByGenre(String genre) throws ServiceException {
		ArrayList<Film> films;
		try {
			startTransaction();
			films = new ArrayList<>(filmDAO.findByGenre(genre));
			commit();
			return films;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding Film", e);
		}
	}

	@Override
	public int getNumberOfAllFilmsPages() throws ServiceException {
		try {
			int numOfFilms = filmDAO.getNumberOfFilms();
			if (numOfFilms % FILMS_ON_PAGE == 0) {
				return numOfFilms / FILMS_ON_PAGE;
			} else {
				return numOfFilms / FILMS_ON_PAGE + 1;
			}

		} catch (DAOException e) {
			throw new ServiceException("Error getting film count", e);
		}
	}
}
