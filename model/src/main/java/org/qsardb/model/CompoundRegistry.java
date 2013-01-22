/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "CompoundRegistry"
)
@XmlType (
	name = "CompoundRegistry"
)
public class CompoundRegistry extends ContainerRegistry<CompoundRegistry, Compound> {

	@XmlElementRef (
		type = Compound.class
	)
	private List<Compound> compounds = new ArrayList<Compound>();


	CompoundRegistry(){
	}

	CompoundRegistry(Qdb qdb){
		super(qdb);
	}

	@Override
	List<Compound> getList(){
		return this.compounds;
	}

	@Override
	String type(){
		return "compounds";
	}
}