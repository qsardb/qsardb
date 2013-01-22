/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public interface Storage {

	/**
	 * @throw FileNotFoundException If the path cannot be found.
	 */
	InputStream getInputStream(String path) throws IOException;

	void add(String path) throws IOException;

	void remove(String path) throws IOException;

	OutputStream getOutputStream(String path) throws IOException;

	void close() throws IOException, QdbException;
}