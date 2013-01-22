/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.qsardb.model.*;

abstract
public class Expression {

	abstract
	public boolean evaluate(Container<?, ?> container);
}