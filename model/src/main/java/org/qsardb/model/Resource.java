/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public interface Resource {

	/**
	 * @return Qdb instance, or <code>null</code>.
	 */
	Qdb getQdb();

	String qdbPath();

	void storeChanges() throws IOException, QdbException;
}