/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

import org.qsardb.model.*;

public class LabelExpression extends QdbExpression {

	private String label = null;


	public LabelExpression(String label){
		setLabel(label);
	}

	@Override
	public boolean evaluate(Container<?, ?> container){
		return (container.getLabels()).contains(getLabel());
	}

	public String getLabel(){
		return this.label;
	}

	public void setLabel(String label){
		this.label = label;
	}
}