/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import org.qsardb.cargo.map.*;
import org.qsardb.model.*;

public class LeverageValuesCargo extends MapCargo<Prediction> {

	protected LeverageValuesCargo(Prediction prediction){
		super(LeverageValuesCargo.ID, prediction);
	}

	@Override
	public String keyName(){
		return "Compound Id";
	}

	@Override
	public String valueName(){
		return "Leverage";
	}

	// XXX
	public static final String ID = LeverageValuesCargo.class.getName();
}