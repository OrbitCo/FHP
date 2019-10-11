package com.fmcna.fhpckd.exception;

public class ServiceException extends Exception {

	public ServiceException(Exception e) {
		super(e);
	}

	public ServiceException( String message ) {
		super( message );
	}
	
	public ServiceException( String message, Exception e) {
		super( message, e);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
