/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.math.*;
import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

@SuppressWarnings (
	value = {"boxing"}
)
public class ScaleFrequencyMapTest {

	@Test
	public void getPattern(){
		ScaleFrequencyMap map = new ScaleFrequencyMap();

		assertEquals("0", map.getPattern());

		map.add(new BigDecimal("2.71828"));
		assertEquals("0.00000", map.getPattern());

		map.add(new BigDecimal("3.14159"));
		assertEquals("0.00000", map.getPattern());
	}

	@Test
	public void getVariablePattern(){
		ScaleFrequencyMap map = new ScaleFrequencyMap();

		map.add(new BigDecimal("1.0"));
		assertEquals("0.0", map.getPattern());

		map.add(new BigDecimal("2.00"));
		assertEquals("0.0#", map.getPattern());

		map.add(new BigDecimal("3.000"));
		assertEquals("0.0##", map.getPattern());

		map.add(new BigDecimal("4.0000"));
		assertEquals("0.0###", map.getPattern());

		map.add(new BigDecimal("5.00000"));
		assertEquals("0.0####", map.getPattern());

		map.add(new BigDecimal("6.000000"));
		assertEquals("0.0####", map.getPattern());
	}

	@Test
	public void getVariablePattern2(){
		ScaleFrequencyMap map = new ScaleFrequencyMap();

		map.add(new BigDecimal("0"));
		assertEquals("0", map.getPattern());

		map.add(new BigDecimal("0.333333"));
		assertEquals("0.####", map.getPattern());
	}

	@Test
	public void integerSample(){
		Collection<Integer> sample = Arrays.asList(1, 2, 3, 4, 5);

		ScaleFrequencyMap map = ScaleFrequencyMap.sample(sample);

		assertEquals(0, map.minScale());
		assertEquals(0, map.maxScale());

		assertEquals(5, map.getCount(0));
	}

	@Test
	public void doubleSample(){
		Collection<Double> sample = Arrays.asList(1D, 2D, 3D, 4D, 5D);

		ScaleFrequencyMap map = ScaleFrequencyMap.sample(sample);

		assertEquals(1, map.minScale());
		assertEquals(1, map.maxScale());

		assertEquals(5, map.getCount(1));
	}

	@Test
	public void doubleStringSample(){
		Collection<String> sample = Arrays.asList("1.0", "2.00", "3.000", "4.0000", "5.00000");

		ScaleFrequencyMap map = ScaleFrequencyMap.sample(sample);

		assertEquals(1, map.minScale());
		assertEquals(5, map.maxScale());

		assertEquals(0, map.getCount(0));

		assertEquals(1, map.getCount(1));
		assertEquals(1, map.getCount(5));
	}
}