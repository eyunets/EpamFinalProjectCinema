package com.epam.training.service.impl;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.NewsDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.NewsDAOImpl;
import com.epam.training.entity.News;
import com.epam.training.service.NewsService;
import com.epam.training.service.TransactionManager;
import com.epam.training.service.exception.ServiceException;

public class NewsServiceImpl extends TransactionManager implements NewsService {

	private static volatile NewsService INSTANCE = null;
	private NewsDAO newsDAO = NewsDAOImpl.getInstance();

	private NewsServiceImpl() {
	}

	public static NewsService getInstance() {
		NewsService NewsService = INSTANCE;
		if (NewsService == null) {
			synchronized (NewsServiceImpl.class) {
				NewsService = INSTANCE;
				if (NewsService == null) {
					INSTANCE = NewsService = new NewsServiceImpl();
				}
			}
		}

		return NewsService;
	}

	@Override
	public News save(News news) throws ServiceException {

		try {
			if (news != null) {
				startTransaction();
				news = newsDAO.save(news);
				commit();
				return news;
			} else {
				throw new ServiceException("News not defined");
			}
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error creating News", e);
		}
	}

	@Override
	public News find(Serializable id) throws ServiceException {
		News news = new News();
		try {
			startTransaction();
			news = newsDAO.find(id);
			commit();
			return news;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding User", e);
		}
	}

	@Override
	public void update(News news) throws ServiceException {
		try {
			startTransaction();
			newsDAO.update(news);
			commit();
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error updating News", e);
		}
	}

	public void update(News oldNews, News newNews) throws ServiceException {
		News news = new News();
		news.setId(oldNews.getId());;
		news.setTime((newNews.getTime() == null) ? oldNews.getTime() : newNews.getTime());
		news.setDate((newNews.getDate() == null) ? oldNews.getDate() : newNews.getDate());
		news.setText((newNews.getText().length() == 0) ? oldNews.getText() : newNews.getText());
		news.setTitle((newNews.getTitle().length() == 0) ? oldNews.getTitle() : newNews.getTitle());
		update(news);
	}

	@Override
	public int delete(Serializable id) throws ServiceException {
		try {
			startTransaction();
			int rows = newsDAO.delete(id);
			commit();
			return rows;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error deleting News", e);
		}
	}

	@Override
	public List<News> findAll() throws ServiceException {
		ArrayList<News> news;
		try {
			startTransaction();
			news = new ArrayList<>(newsDAO.findAll());
			commit();
			return news;
		} catch (DAOException e) {
			rollback();
			throw new ServiceException("Error finding News", e);
		}
	}

	@Override
	public List<News> findNewsByYear(LocalDate year) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
}
