/*
 * Copyright (c) 2009 Univeristy of Tartu
 */
package org.qsardb.cargo.map;

import org.qsardb.model.*;

class PropertyCargo extends MapCargo<Property> {

	PropertyCargo(Property property){
		super(ID, property);
	}

	@Override
	public String keyName(){
		return "A key";
	}

	@Override
	public String valueName(){
		return "A value";
	}

	public static final String ID = "propertycargo";
}