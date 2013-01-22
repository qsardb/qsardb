/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public class TempFilePayload implements Payload, Closeable {

	private File file = null;


	public TempFilePayload() throws IOException {
		this(null);
	}

	public TempFilePayload(File dir) throws IOException {
		this.file =  File.createTempFile("payload", ".tmp", dir);
	}

	public File getFile() throws IOException {

		if(this.file == null){
			throw new IOException();
		}

		return this.file;
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(getFile());
	}

	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(getFile());
	}

	public void close(){
		this.file.delete();

		this.file = null;
	}
}