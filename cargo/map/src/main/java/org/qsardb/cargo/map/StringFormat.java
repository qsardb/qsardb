/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

public class StringFormat extends ValueFormat<String> {

	@Override
	protected String formatValue(String value){
		return value;
	}

	@Override
	protected String parseString(String string){
		return string;
	}
}