/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

import org.qsardb.model.*;

public class ValuesCargo extends MapCargo<Parameter> {

	protected ValuesCargo(Descriptor descriptor){
		super(ValuesCargo.ID, descriptor);
	}

	protected ValuesCargo(Prediction prediction){
		super(ValuesCargo.ID, prediction);
	}

	protected ValuesCargo(Property property){
		super(ValuesCargo.ID, property);
	}

	@Override
	protected String keyName(){
		return "Compound Id";
	}

	@Override
	protected String valueName(){
		return getContainer().getId();
	}

	public static final String ID = "values";
}
