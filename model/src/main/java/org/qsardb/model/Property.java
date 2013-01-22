/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Property"
)
@XmlType (
	name = "Property"
)
public class Property extends Parameter<PropertyRegistry, Property> {

	@XmlElement (
		name = "Endpoint",
		required = false
	)
	private String endpoint = null;

	@XmlElement (
		name = "Species",
		required = false
	)
	private String species = null;


	@Deprecated
	public Property(){
	}

	public Property(String id){
		super(id);
	}

	public String getEndpoint(){
		return this.endpoint;
	}

	public void setEndpoint(String endpoint){
		this.endpoint = endpoint;
	}

	public String getSpecies(){
		return this.species;
	}

	public void setSpecies(String species){
		this.species = species;
	}
}