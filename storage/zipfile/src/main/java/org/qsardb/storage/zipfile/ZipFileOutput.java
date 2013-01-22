/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.io.*;
import java.util.zip.*;

import org.qsardb.model.*;

/**
 * Writes data to a Zip file. This storage does not support read access and
 * can be used with Qdb.copyTo method.
 *
 * <p>
 * Every FileOutput instance must be {@link #close() closed} explicitly after use.
 */
public class ZipFileOutput extends ZipFileStorage {

	private ZipOutputStream zipOs = null;


	public ZipFileOutput(File file) throws IOException {
		this(new FileOutputStream(file));
	}

	public ZipFileOutput(OutputStream os){
		this.zipOs = new ZipOutputStream(os);
	}

	protected ZipOutputStream getZipOutputStream() throws IOException {

		if(this.zipOs == null){
			throw new IOException("The storage is closed");
		}

		return this.zipOs;
	}

	/**
	 * Sets the compression level for subsequent entries.
	 *
	 * @param level The compression level in range between <code>0</code> and <code>9</code>,
	 * where <code>0</code> means no compression (fastest) and <code>9</code> means the best compression (slowest).
	 */
	public void setLevel(int level) throws IOException {
		getZipOutputStream().setLevel(level);
	}

	@Override
	public OutputStream getOutputStream(final String path) throws IOException {
		return new FilterOutputStream(getZipOutputStream()){

			{
				((ZipOutputStream)this.out).putNextEntry(new ZipEntry(path));
			}

			@Override
			public void close() throws IOException {
				flush();

				((ZipOutputStream)this.out).closeEntry();
			}
		};
	}

	@SuppressWarnings (
		value = {"unused"}
	)
	public void close() throws IOException, QdbException {

		try {
			if(this.zipOs != null){
				this.zipOs.close();
			}
		} finally {
			this.zipOs = null;
		}
	}
}
