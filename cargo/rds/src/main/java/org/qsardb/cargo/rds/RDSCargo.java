/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.rds;

import java.io.*;

import org.qsardb.model.*;

/**
 * @see Context#startEngine
 * @see Context#stopEngine
 */
public class RDSCargo extends Cargo<Model> {

	public RDSCargo(Model model){
		super(ID, model);
	}

	@Override
	public boolean isBinary(){
		return true;
	}

	public RDSObject loadRdsObject() throws IOException {
		File file = File.createTempFile("cargo", ".rds");

		OutputStream os = new FileOutputStream(file);

		try {
			InputStream is = getInputStream();

			try {
				copy(is, os);
			} finally {
				is.close();
			}
		} finally {
			os.close();
		}

		return new RDSObject(Context.getEngine(), file);
	}

	public void saveRdsObject(RDSObject object) throws IOException {
		File file = object.getFile();

		InputStream is = new FileInputStream(file);

		try {
			OutputStream os = getOutputStream();

			try {
				copy(is, os);
			} finally {
				os.close();
			}
		} finally {
			is.close();
		}
	}

	public static final String ID = "rds";
}