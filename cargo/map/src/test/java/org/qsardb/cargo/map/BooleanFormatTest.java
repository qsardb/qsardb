/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.cargo.map;

import org.junit.*;

import static org.junit.Assert.*;

public class BooleanFormatTest {

	@Test
	public void formatValue(){
		BooleanFormat format = new BooleanFormat();

		assertEquals("true", format.format(Boolean.TRUE));
		assertEquals("false", format.format(Boolean.FALSE));

		assertEquals(ValueFormat.NA, format.format(null));
	}

	@Test
	public void parseValue(){
		BooleanFormat format = new BooleanFormat();

		assertEquals(Boolean.TRUE, format.parse("true"));
		assertEquals(Boolean.TRUE, format.parse("TRUE"));
		assertEquals(Boolean.TRUE, format.parse("yes"));
		assertEquals(Boolean.TRUE, format.parse("YES"));

		assertEquals(Boolean.FALSE, format.parse("false"));
		assertEquals(Boolean.FALSE, format.parse("FALSE"));
		assertEquals(Boolean.FALSE, format.parse("no"));
		assertEquals(Boolean.FALSE, format.parse("NO"));

		assertEquals(null, format.parse(ValueFormat.NA));
	}
}