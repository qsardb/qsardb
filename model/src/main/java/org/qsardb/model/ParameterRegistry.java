/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlType (
	name = "ParameterRegistry"
)
abstract
public class ParameterRegistry <R extends ParameterRegistry<R, C>, C extends Parameter<R, C>> extends ContainerRegistry<R, C> {

	ParameterRegistry(){
	}

	ParameterRegistry(Qdb qdb){
		super(qdb);
	}
}