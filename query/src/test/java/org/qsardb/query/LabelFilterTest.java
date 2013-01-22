/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import java.util.*;

import org.qsardb.model.*;

import org.junit.*;

import static org.junit.Assert.*;

public class LabelFilterTest {

	private Compound compound;


	@Test
	public void orExpression() throws Exception {
		assertTrue(match("first OR second"));
		assertTrue(match("second OR third"));
		assertTrue(match("first OR third"));

		assertTrue(match("first OR second AND third"));
		assertTrue(match("(first OR second) AND third"));
	}

	@Test
	public void andExpression() throws Exception {
		assertFalse(match("first AND second"));
		assertTrue(match("first AND NOT second"));
		assertFalse(match("second AND third"));
		assertTrue(match("NOT second AND third"));
		assertTrue(match("first AND third"));

		assertTrue(match("first AND second OR third"));
		assertTrue(match("first AND (second OR third)"));
	}

	private boolean match(String string) throws Exception {
		LabelFilter<Compound> filter = new LabelFilter<Compound>(string);

		return filter.accept(this.compound);
	}

	@Before
	public void begin(){
		this.compound = new Compound("1");

		List<String> labels = Arrays.asList("first", "third");

		this.compound.getLabels().addAll(labels);
	}

	@After
	public void end(){
		this.compound = null;
	}
}