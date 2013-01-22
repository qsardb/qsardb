/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.qsardb.model.*;

public class ParenthesizedExpression extends Expression {

	private Expression expression = null;


	public ParenthesizedExpression(Expression expression){
		setExpression(expression);
	}

	@Override
	public boolean evaluate(Container<?, ?> container){
		return getExpression().evaluate(container);
	}

	public Expression getExpression(){
		return this.expression;
	}

	public void setExpression(Expression expression){
		this.expression = expression;
	}
}