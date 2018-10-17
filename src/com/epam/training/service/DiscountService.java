package com.epam.training.service;

import com.epam.training.entity.Discount;
import com.epam.training.entity.User;
import com.epam.training.service.exception.ServiceException;

public interface DiscountService extends Service<Discount> {
	Discount findByUser(User user) throws ServiceException;

}
