/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

public class ReferencesCargo extends MapCargo<Parameter> {

	protected ReferencesCargo(Descriptor descriptor){
		super(ReferencesCargo.ID, descriptor);
	}

	protected ReferencesCargo(Prediction prediction){
		super(ReferencesCargo.ID, prediction);
	}

	protected ReferencesCargo(Property property){
		super(ReferencesCargo.ID, property);
	}

	@Override
	protected String keyName(){
		return "Compound Id";
	}

	@Override
	protected String valueName(){
		return "BibTeX entry key(s)";
	}

	public Map<String, String> loadReferences() throws IOException {
		return loadStringMap();
	}

	public void storeReferences(Map<String, String> references) throws IOException {
		storeStringMap(references);
	}

	public static final String ID = "references";
}