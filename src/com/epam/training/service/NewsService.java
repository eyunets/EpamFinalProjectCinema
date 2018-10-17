package com.epam.training.service;

import java.time.LocalDate;
import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.News;

public interface NewsService extends Service<News> {

	List<News> findNewsByYear(LocalDate year) throws DAOException;
}
