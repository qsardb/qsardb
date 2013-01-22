/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

public class QdbException extends Exception {

	public QdbException(String message){
		super(message);
	}

	public QdbException(Throwable cause){
		super(cause);
	}

	public QdbException(String message, Throwable cause){
		super(message, cause);
	}
}