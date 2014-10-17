/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

abstract
public class MatrixCargo extends Cargo<Model> {

	public MatrixCargo(String id, Model model){
		super(id, model);
	}

	abstract
	public Map<Compound, Double> prepare(List<Compound> compounds, List<Descriptor> descriptors) throws IOException;

	abstract
	public Double getCriticalValue(List<Compound> compounds) throws IOException, ClassNotFoundException;

	abstract
	public Map<Compound, Double> predict(List<Compound> compounds) throws IOException, ClassNotFoundException;

	public double predict(Map<String, Double> descriptorValues) throws IOException, ClassNotFoundException {
		throw new UnsupportedOperationException();
	}

	public Object loadObject() throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(getInputStream());

		try {
			return is.readObject();
		} finally {
			is.close();
		}
	}

	public void storeObject(Object object) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(getOutputStream());

		try {
			os.writeObject(object);
		} finally {
			os.close();
		}
	}
}