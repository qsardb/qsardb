/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.rds;

import java.io.*;

import org.rosuda.JRI.*;

public class Context {

	private Context(){
	}

	static
	public Rengine getEngine(){
		return Context.engine;
	}

	static
	public void startEngine() throws IOException {
		startEngine(new LogCallback());
	}

	static
	public void startEngine(RMainLoopCallbacks callback) throws IOException {
		Rengine engine = new Rengine(new String[]{"--no-restore", "--no-save", "--quiet"}, false, callback);
		if(!engine.waitForR()){
			throw new IOException();
		}

		Context.engine = engine;
	}

	static
	public void stopEngine(){
		Rengine engine = Context.engine;

		try {
			if(engine != null){
				engine.end();
			}
		} finally {
			Context.engine = null;
		}
	}

	private static Rengine engine = null;
}