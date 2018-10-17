package com.epam.training.service;

import java.util.List;

import com.epam.training.entity.Film;
import com.epam.training.service.exception.ServiceException;

public interface FilmService extends Service<Film> {

	List<Film> findByName(String name) throws ServiceException;

	List<Film> findByGenre(String genre) throws ServiceException;

	void update(Film oldFilm, Film newFilm) throws ServiceException;

	int getNumberOfAllFilmsPages() throws ServiceException;

	List<Film> findAllByPage(int pageNum) throws ServiceException;
}
