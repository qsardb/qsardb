/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public class FileUtil {

	private FileUtil(){
	}

	static
	public File createTempDirectory(){
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		String name = "qdb" + "-" + String.valueOf(System.currentTimeMillis());

		for(int i = 0; true; i++){
			File dir = new File(tmpDir, i == 0 ? name : (name + "-" + i));

			if(dir.mkdir()){
				return dir;
			}
		}
	}

	static
	public void deleteTempDirectory(File dir) throws IOException {
		deleteDirectoryContents(dir);

		boolean success = dir.delete();
		if(!success){
			throw new IOException("Unable to delete temporary directory "+dir);
		}
	}

	static
	private void deleteDirectoryContents(File dir) throws IOException {
		File[] files = dir.listFiles();

		for(File file : files){

			if(file.isDirectory()){
				deleteDirectoryContents(file);
			}

			file.delete();
		}
	}
}