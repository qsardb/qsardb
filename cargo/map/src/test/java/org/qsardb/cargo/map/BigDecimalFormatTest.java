/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.math.*;

import org.junit.*;

import static org.junit.Assert.*;

public class BigDecimalFormatTest {

	@Test
	public void formatValue(){
		BigDecimalFormat format = new BigDecimalFormat();

		assertEquals(ValueFormat.NA, format.format(null));
	}

	@Test
	public void parseValue(){
		BigDecimalFormat format = new BigDecimalFormat();

		assertEquals(format.parse("1.0"), format.parse("1,0"));

		assertEquals(null, format.parse(ValueFormat.NA));
	}

	@Test
	public void toBigDecimal(){
		assertEquals(BigDecimal.ONE, BigDecimalFormat.toBigDecimal(BigDecimal.ONE));

		assertEquals(null, BigDecimalFormat.toBigDecimal(Double.valueOf(Double.NaN)));
		assertEquals(new BigDecimal("1.0"), BigDecimalFormat.toBigDecimal(Double.valueOf(1D)));

		assertEquals(null, BigDecimalFormat.toBigDecimal(Float.valueOf(Float.NaN)));
		assertEquals(new BigDecimal("1.0"), BigDecimalFormat.toBigDecimal(Float.valueOf(1F)));

		assertEquals(BigDecimal.ONE, BigDecimalFormat.toBigDecimal("1"));
	}
}