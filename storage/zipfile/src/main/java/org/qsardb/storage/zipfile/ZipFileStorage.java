/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.io.*;

import org.qsardb.model.*;

/**
 * Abstract base class for Zip file storage.
 */
abstract
public class ZipFileStorage implements Storage {

	public InputStream getInputStream(String path) throws IOException {
		throw new IOException("Read not supported");
	}

	public void add(String path) throws IOException {
		throw new IOException("Add not supported");
	}

	public void remove(String path) throws IOException {
		throw new IOException("Remove not supported");
	}

	public OutputStream getOutputStream(String path) throws IOException {
		throw new IOException("Write not supported");
	}
}
