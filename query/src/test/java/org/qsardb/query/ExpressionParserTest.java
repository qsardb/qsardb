/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.junit.*;

public class ExpressionParserTest {

	private ExpressionParser parser = null;


	@Test
	public void hasExpression() throws ParseException {
		this.parser.parse("label");
		this.parser.parse("(label)");
		this.parser.parse("( label )");
	}

	@Test
	public void hasNotExpression1() throws ParseException {
		this.parser.parse("!label");
		this.parser.parse("(!label)");

		this.parser.parse("! label");
		this.parser.parse("(! label)");
	}

	@Test
	public void hasNotExpression2() throws ParseException {
		this.parser.parse("not label");
		this.parser.parse("(not label)");

		this.parser.parse("Not label");
		this.parser.parse("(Not label)");

		this.parser.parse("NOT label");
		this.parser.parse("(NOT label)");
	}

	@Test
	public void orExpression() throws ParseException {
		this.parser.parse("left | right");
		this.parser.parse("(left | right)");

		this.parser.parse("left OR right");
		this.parser.parse("(left OR right)");
	}

	@Test
	public void andExpression() throws ParseException {
		this.parser.parse("left & right");
		this.parser.parse("(left & right)");

		this.parser.parse("left AND right");
		this.parser.parse("(left AND right)");
	}

	@Before
	public void initializeParser(){
		this.parser = new ExpressionParser();
	}

	@After
	public void nullifyParser(){
		this.parser = null;
	}
}