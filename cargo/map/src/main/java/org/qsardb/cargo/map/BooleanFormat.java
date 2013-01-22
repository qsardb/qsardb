/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

public class BooleanFormat extends ValueFormat<Boolean> {

	@Override
	protected String formatValue(Boolean value){
		return value.booleanValue() ? "true" : "false";
	}

	/**
	 * @throws IllegalArgumentException
	 */
	@Override
	protected Boolean parseString(String string){

		if(string.equalsIgnoreCase("true") || string.equalsIgnoreCase("yes")){
			return Boolean.TRUE;
		} else

		if(string.equalsIgnoreCase("false") || string.equalsIgnoreCase("no")){
			return Boolean.FALSE;
		}

		throw new IllegalArgumentException(string);
	}
}