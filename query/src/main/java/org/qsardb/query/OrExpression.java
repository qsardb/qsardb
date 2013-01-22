/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.qsardb.model.*;

public class OrExpression extends BinaryExpression {

	public OrExpression(Expression left, Expression right){
		super(left, right);
	}

	@Override
	public boolean evaluate(Container<?, ?> container){
		return getLeft().evaluate(container) | getRight().evaluate(container);
	}
}