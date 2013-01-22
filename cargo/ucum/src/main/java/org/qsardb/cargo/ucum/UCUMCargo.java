/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.ucum;

import java.io.*;
import java.text.*;

import javax.measure.unit.*;

import org.qsardb.model.*;

public class UCUMCargo extends Cargo<Parameter> {

	protected UCUMCargo(Descriptor descriptor){
		super(UCUMCargo.ID, descriptor);
	}

	protected UCUMCargo(Prediction prediction){
		super(UCUMCargo.ID, prediction);
	}

	protected UCUMCargo(Property property){
		super(UCUMCargo.ID, property);
	}

	@Override
	public boolean isBinary(){
		return false;
	}

	public Unit<?> loadUnit() throws IOException, QdbException {
		UnitFormat format = getUnitFormat();

		try {
			return format.parse(loadString(), new ParsePosition(0));
		} catch(IllegalArgumentException iae){
			throw new QdbException(iae);
		}
	}

	public void storeUnit(Unit<?> unit) throws IOException {
		UnitFormat format = getUnitFormat();

		storeString(format.format(unit));
	}

	static
	private UnitFormat getUnitFormat(){
		return UnitFormat.getInstance();
	}

	public static final String ID = "ucum";
}