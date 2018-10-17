package com.epam.training.dao;

import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Film;

public interface FilmDAO extends DAO<Film> {

	List<Film> findByName(String name) throws DAOException;

	List<Film> findByGenre(String genre) throws DAOException;

	int getNumberOfFilms() throws DAOException;

	List<Film> findAllByPage(int start, int amount) throws DAOException;

}
