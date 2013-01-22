/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Descriptor"
)
@XmlType (
	name = "Descriptor"
)
public class Descriptor extends Parameter<DescriptorRegistry, Descriptor> {

	@XmlElement (
		name = "Application",
		required = false
	)
	private String application = null;


	@Deprecated
	public Descriptor(){
	}

	public Descriptor(String id){
		super(id);
	}

	public String getApplication(){
		return this.application;
	}

	public void setApplication(String application){
		this.application = application;
	}
}