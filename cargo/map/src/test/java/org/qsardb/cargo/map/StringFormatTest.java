/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import org.junit.*;

import static org.junit.Assert.*;

public class StringFormatTest {

	@Test
	public void formatValue(){
		StringFormat format = new StringFormat();

		assertEquals(ValueFormat.NA, format.format(null));
	}

	@Test
	public void parseValue(){
		StringFormat format = new StringFormat();

		assertEquals(null, format.parse(ValueFormat.NA));
	}
}