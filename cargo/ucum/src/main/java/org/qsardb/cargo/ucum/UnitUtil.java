/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.cargo.ucum;

import javax.measure.converter.*;
import javax.measure.quantity.*;
import javax.measure.unit.*;

public class UnitUtil {

	private UnitUtil(){
	}

	static
	public Unit<? extends Quantity> log(Unit<? extends Quantity> unit){
		return unit.transform(new LogConverter(10));
	}

	static
	public Unit<? extends Quantity> p(Unit<? extends Quantity> unit){
		return log((Unit)unit.inverse());
	}
}