/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.cargo.map;

import java.text.*;
import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public class DoubleFormatTest {

	@Test
	public void formatValue(){
		DoubleFormat format = new DoubleFormat();

		assertEquals(ValueFormat.NA, format.format(null));
		assertEquals(ValueFormat.NA, format.format(Double.valueOf(Double.NaN)));
	}

	@Test
	public void parseValue(){
		DoubleFormat format = new DoubleFormat();

		assertEquals(format.parse("1.0"), format.parse("1,0"));

		assertEquals(Double.valueOf(Double.NaN), format.parse(ValueFormat.NA));
	}

	@Test
	public void compare(){
		DoubleFormat first = new DoubleFormat();
		DoubleFormat second = new DoubleFormat("0.00");

		assertTrue((first).equals(new DoubleFormat()));
		assertTrue((second).equals(new DoubleFormat("0.00")));

		assertFalse((first).equals(second));

		assertFalse((first).equals(new DoubleFormat(){/* empty class body */}));
		assertFalse((second).equals(new DoubleFormat("0.00"){/* empty class body */}));
	}

	@Test
	public void constructForUS(){
		new DoubleFormat(new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US)));
	}

	@Test (
		expected = IllegalArgumentException.class
	)
	public void constructForFrance(){
		new DoubleFormat(new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.FRANCE)));
	}
}