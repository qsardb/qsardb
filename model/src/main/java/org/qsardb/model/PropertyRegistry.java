/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "PropertyRegistry"
)
@XmlType (
	name = "PropertyRegistry"
)
public class PropertyRegistry extends ParameterRegistry<PropertyRegistry, Property> {

	@XmlElementRef (
		type = Property.class
	)
	private List<Property> properties = new ArrayList<Property>();


	PropertyRegistry(){
	}

	PropertyRegistry(Qdb qdb){
		super(qdb);
	}

	@Override
	List<Property> getList(){
		return this.properties;
	}

	@Override
	String type(){
		return "properties";
	}
}