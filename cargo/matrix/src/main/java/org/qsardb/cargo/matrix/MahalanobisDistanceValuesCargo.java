/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import org.qsardb.cargo.map.*;
import org.qsardb.model.*;

public class MahalanobisDistanceValuesCargo extends MapCargo<Prediction> {

	protected MahalanobisDistanceValuesCargo(Prediction prediction){
		super(ID, prediction);
	}

	@Override
	public String keyName(){
		return "Compound Id";
	}

	@Override
	public String valueName(){
		return "Mahalanobis distance";
	}

	// XXX
	public static final String ID = MahalanobisDistanceValuesCargo.class.getName();
}