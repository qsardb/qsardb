/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.io.*;
import java.util.zip.*;

/**
 * Reads data from a Zip file. This storage does not support writing.
 */
public class ZipFileInput extends ZipFileStorage {

	private ZipFile zipFile = null;


	public ZipFileInput(File file) throws IOException {
		this.zipFile = new ZipFile(file);
	}

	protected ZipFile getZipFile() throws IOException {

		if(this.zipFile == null){
			throw new IOException("The storage is closed");
		}

		return this.zipFile;
	}

	@Override
	public InputStream getInputStream(final String path) throws IOException {
		InputStream is = getZipFile().getInputStream(new ZipEntry(path));

		if(is == null){
			throw new FileNotFoundException(path);
		}

		return is;
	}

	public void close() throws IOException {
		try {
			if(this.zipFile != null){
				this.zipFile.close();
			}
		} finally {
			this.zipFile = null;
		}
	}
}
