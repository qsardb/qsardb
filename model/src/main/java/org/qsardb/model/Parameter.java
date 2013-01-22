/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlType (
	name = "Parameter"
)
abstract
public class Parameter<R extends ParameterRegistry<R, C>, C extends Parameter<R, C>> extends Container<R, C> {

	Parameter(){
	}

	public Parameter(String id){
		super(id);
	}
}