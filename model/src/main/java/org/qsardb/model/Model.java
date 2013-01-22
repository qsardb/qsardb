/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Model"
)
@XmlType (
	name = "Model"
)
public class Model extends Container<ModelRegistry, Model> {

	@XmlIDREF
	@XmlElement (
		name = "PropertyId",
		required = true
	)
	private Property property = null;


	@Deprecated
	public Model(){
	}

	public Model(String id, Property property){
		super(id);

		setProperty(property);
	}

	public Property getProperty(){
		return this.property;
	}

	public void setProperty(Property property){
		this.property = property;
	}
}