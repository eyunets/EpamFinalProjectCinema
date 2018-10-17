package com.epam.training.dao;

import java.time.LocalDate;
import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.News;

public interface NewsDAO extends DAO<News> {

	List<News> findNewsByYear(LocalDate year) throws DAOException;

}
