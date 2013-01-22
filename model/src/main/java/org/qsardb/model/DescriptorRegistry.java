/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "DescriptorRegistry"
)
@XmlType (
	name = "DescriptorRegistry"
)
public class DescriptorRegistry extends ParameterRegistry<DescriptorRegistry, Descriptor> {

	@XmlElementRef (
		type = Descriptor.class
	)
	private List<Descriptor> descriptors = new ArrayList<Descriptor>();


	DescriptorRegistry(){
	}

	DescriptorRegistry(Qdb qdb){
		super(qdb);
	}

	@Override
	List<Descriptor> getList(){
		return this.descriptors;
	}

	@Override
	String type(){
		return "descriptors";
	}
}