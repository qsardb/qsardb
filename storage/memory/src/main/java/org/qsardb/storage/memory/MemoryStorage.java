/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.memory;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

/**
 * Read/write access to storage that is backed by random access memory (RAM).
 * This storage will not persist after the program exits.
 */
public class MemoryStorage implements Storage {

	private Map<String, byte[]> cache = new HashMap<String, byte[]>();


	protected Map<String, byte[]> getCache() throws IOException {

		if(this.cache == null){
			throw new IOException("The storage is closed");
		}

		return this.cache;
	}

	public InputStream getInputStream(final String path) throws IOException {
		Map<String, byte[]> cache = getCache();

		byte[] bytes = cache.get(path);

		if(bytes == null){
			throw new FileNotFoundException(path);
		}

		return new ByteArrayInputStream(bytes);
	}

	public void add(String path) throws IOException {
		Map<String, byte[]> cache = getCache();

		cache.put(path, new byte[0]);
	}

	public void remove(String path) throws IOException {
		Map<String, byte[]> cache = getCache();

		cache.remove(path);
	}

	public OutputStream getOutputStream(final String path){
		return new ByteArrayOutputStream(16 * 1024){

			@Override
			public void close() throws IOException {
				super.close();

				Map<String, byte[]> cache = getCache();

				cache.put(path, toByteArray());
			}
		};
	}

	public void close(){

		if(this.cache != null){
			this.cache.clear();
		}

		this.cache = null;
	}
}
