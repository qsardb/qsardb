/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.qsardb.model.*;

public class LabelFilter<C extends Container<?, C>> implements ContainerFilter<C> {

	private Expression expression = null;


	public LabelFilter(String string) throws Exception {
		this(parse(string));
	}

	public LabelFilter(Expression expression){
		setExpression(expression);
	}

	public boolean accept(C container){
		return getExpression().evaluate(container);
	}

	public Expression getExpression(){
		return this.expression;
	}

	private void setExpression(Expression expression){
		this.expression = expression;
	}

	static
	private Expression parse(String string) throws Exception {
		ExpressionParser parser = new ExpressionParser();

		return parser.parse(string);
	}
}