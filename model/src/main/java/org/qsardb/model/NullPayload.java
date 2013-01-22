/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

class NullPayload implements Payload {

	/**
	 * @throws IOException Always.
	 */
	public InputStream getInputStream() throws IOException {
		throw new IOException();
	}
}