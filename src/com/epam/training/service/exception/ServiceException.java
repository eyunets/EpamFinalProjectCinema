package com.epam.training.service.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -8589667230017436847L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Exception e) {
		super(message, e);
	}
}
