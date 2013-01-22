/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public class FilePayload implements Payload {

	private File file = null;


	public FilePayload(File file){
		this.file = file;
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}
}