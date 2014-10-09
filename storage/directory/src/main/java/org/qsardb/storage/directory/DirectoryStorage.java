/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.directory;

import java.io.*;

import org.qsardb.model.*;

/**
  * Read/write access to storage that is backed by directory in file system.
  */
public class DirectoryStorage implements Storage {

	private File dir = null;


	/**
	 * Constructs DirectoryStorage that is backed by temporary directory.
	 * The default location of temporatory directory is defined by the
	 * java.io.tmpdir system property.
	 */
	public DirectoryStorage() throws IOException {
		this(FileUtil.createTempDirectory());
	}

	public DirectoryStorage(File dir) throws IOException {

		if(!dir.isDirectory()){
			throw new IOException("Path " + dir + " does not denote an existing directory");
		}

		this.dir = dir;
	}

	protected File getDirectory() throws IOException {

		if(this.dir == null){
			throw new IOException("The storage is closed");
		}

		return this.dir;
	}

	protected File getFile(String path) throws IOException {
		return new File(getDirectory(), path);
	}

	public InputStream getInputStream(String path) throws IOException {
		File file = getFile(path);

		return new FileInputStream(file);
	}

	public void add(String path) throws IOException {
		File file = getFile(path);

		addDirectory(file.getParentFile());

		if(!file.exists() && !file.createNewFile()){
			throw new IOException("Failed to add file " + file);
		}
	}

	public void remove(String path) throws IOException {
		File file = getFile(path);

		if(file.exists() && !file.delete()){
			throw new IOException("Failed to remove file " + file);
		}

		removeDirectory(file.getParentFile());
	}

	public OutputStream getOutputStream(String path) throws IOException {
		File file = getFile(path);

		addDirectory(file.getParentFile());

		return new FileOutputStream(file);
	}

	public void close(){
		this.dir = null;
	}

	static
	private void addDirectory(File dir) throws IOException {

		if(!dir.exists() && !dir.mkdirs()){
			throw new IOException();
		}
	}

	static
	private void removeDirectory(File dir){

		while(dir != null && dir.isDirectory()){

			if(dir.list().length > 0 || !dir.delete()){
				return;
			}

			dir = dir.getParentFile();
		}
	}
}
