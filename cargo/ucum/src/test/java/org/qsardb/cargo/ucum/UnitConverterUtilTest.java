/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.cargo.ucum;

import java.math.*;

import javax.measure.converter.*;
import javax.measure.unit.*;

import org.junit.*;

import static org.junit.Assert.*;

public class UnitConverterUtilTest {

	@Test
	public void identityConversion() throws ConversionException {
		assertEquals(new BigDecimal("1"), convert(MOLES_PER_LITRE, MOLES_PER_LITRE, new BigDecimal("1")));
	}

	@Test
	public void rationalConversion() throws ConversionException {
		assertEquals(new BigDecimal("1000"), convert(MOLES_PER_LITRE, MILLIMOLES_PER_LITRE, new BigDecimal("1")));
		assertEquals(new BigDecimal("1"), convert(MILLIMOLES_PER_LITRE, MOLES_PER_LITRE, new BigDecimal("1000")));
	}

	@Test
	public void fromLogConversion() throws ConversionException {
		assertEquals(new BigDecimal("0.1"), convert(UnitUtil.log(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("-1")));
		assertEquals(new BigDecimal("1"), convert(UnitUtil.log(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("0")));
		assertEquals(new BigDecimal("10"), convert(UnitUtil.log(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("1")));

		assertEquals(new BigDecimal("100"), convert(UnitUtil.log(MOLES_PER_LITRE), MILLIMOLES_PER_LITRE, new BigDecimal("-1")));
		assertEquals(new BigDecimal("1000"), convert(UnitUtil.log(MOLES_PER_LITRE), MILLIMOLES_PER_LITRE, new BigDecimal("0")));
		assertEquals(new BigDecimal("10000"), convert(UnitUtil.log(MOLES_PER_LITRE), MILLIMOLES_PER_LITRE, new BigDecimal("1")));

		assertEquals(new BigDecimal("0.0001"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("-1")));
		assertEquals(new BigDecimal("0.001"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("0")));
		assertEquals(new BigDecimal("0.01"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("1")));
	}

	@Test
	public void fromPConversion() throws Exception {
		assertEquals(new BigDecimal("10"), convert(UnitUtil.p(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("-1")));
		assertEquals(new BigDecimal("1"), convert(UnitUtil.p(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("0")));
		assertEquals(new BigDecimal("0.1"), convert(UnitUtil.p(MOLES_PER_LITRE), MOLES_PER_LITRE, new BigDecimal("1")));
	}

	@Test
	public void toLogConversion() throws ConversionException {
		assertEquals(new BigDecimal("-1"), convert(MOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("0.1")));
		assertEquals(new BigDecimal("0"), convert(MOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("1")));
		assertEquals(new BigDecimal("1"), convert(MOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("10")));

		assertEquals(new BigDecimal("-1"), convert(MILLIMOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("100")));
		assertEquals(new BigDecimal("0"), convert(MILLIMOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("1000")));
		assertEquals(new BigDecimal("1"), convert(MILLIMOLES_PER_LITRE, UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("10000")));

		assertEquals(new BigDecimal("-1"), convert(MOLES_PER_LITRE, UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("0.0001")));
		assertEquals(new BigDecimal("0"), convert(MOLES_PER_LITRE, UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("0.001")));
		assertEquals(new BigDecimal("1"), convert(MOLES_PER_LITRE, UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("0.01")));
	}

	@Test
	public void toPConversion() throws ConversionException {
		assertEquals(new BigDecimal("-1"), convert(MOLES_PER_LITRE, UnitUtil.p(MOLES_PER_LITRE), new BigDecimal("10")));
		assertEquals(new BigDecimal("0"), convert(MOLES_PER_LITRE, UnitUtil.p(MOLES_PER_LITRE), new BigDecimal("1")));
		assertEquals(new BigDecimal("1"), convert(MOLES_PER_LITRE, UnitUtil.p(MOLES_PER_LITRE), new BigDecimal("0.1")));
	}

	@Test
	public void logLogConversion() throws ConversionException {
		assertEquals(new BigDecimal("1"), convert(UnitUtil.log(MOLES_PER_LITRE), UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("1")));

		assertEquals(new BigDecimal("2"), convert(UnitUtil.log(MOLES_PER_LITRE), UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("-1")));
		assertEquals(new BigDecimal("3"), convert(UnitUtil.log(MOLES_PER_LITRE), UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("0")));
		assertEquals(new BigDecimal("4"), convert(UnitUtil.log(MOLES_PER_LITRE), UnitUtil.log(MILLIMOLES_PER_LITRE), new BigDecimal("1")));

		assertEquals(new BigDecimal("-4"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("-1")));
		assertEquals(new BigDecimal("-3"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("0")));
		assertEquals(new BigDecimal("-2"), convert(UnitUtil.log(MILLIMOLES_PER_LITRE), UnitUtil.log(MOLES_PER_LITRE), new BigDecimal("1")));
	}

	static
	private void assertEquals(BigDecimal expected, BigDecimal actual){
		assertTrue((expected.round(CONTEXT)).compareTo(actual.round(CONTEXT)) == 0);
	}

	static
	private BigDecimal convert(Unit<?> from, Unit<?> to, BigDecimal value) throws ConversionException {
		UnitConverter converter = UnitConverterUtil.getConverter(from, to);

		return converter.convert(value, CONTEXT);
	}

	private static final Unit<?> MOLES_PER_LITRE = (SI.MOLE).divide(NonSI.LITRE);
	private static final Unit<?> MILLIMOLES_PER_LITRE = (SI.MetricPrefix.MILLI(SI.MOLE)).divide(NonSI.LITRE);

	private static final MathContext CONTEXT = new MathContext(8, RoundingMode.HALF_DOWN);
}