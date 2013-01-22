/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.memory;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

public class SimpleMemoryStorage implements Storage {

	private Map<String, byte[]> cache = new HashMap<String, byte[]>();


	public InputStream getInputStream(final String path) throws IOException {
		byte[] bytes = SimpleMemoryStorage.this.cache.get(path);

		if(bytes == null){
			throw new FileNotFoundException(path);
		}

		return new ByteArrayInputStream(bytes);
	}

	public void add(String path){
	}

	public void remove(String path){
	}

	public OutputStream getOutputStream(final String path){
		return new ByteArrayOutputStream(){

			@Override
			public void close() throws IOException {
				super.close();

				SimpleMemoryStorage.this.cache.put(path, toByteArray());
			}
		};
	}

	public void close(){
		SimpleMemoryStorage.this.cache.clear();
	}
}