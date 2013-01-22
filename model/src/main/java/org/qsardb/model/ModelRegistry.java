/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "ModelRegistry"
)
@XmlType (
	name = "ModelRegistry"
)
public class ModelRegistry extends ContainerRegistry<ModelRegistry, Model> {

	@XmlElementRef (
		type = Model.class
	)
	private List<Model> models = new ArrayList<Model>();


	ModelRegistry(){
	}

	ModelRegistry(Qdb qdb){
		super(qdb);
	}

	@Override
	List<Model> getList(){
		return this.models;
	}

	@Override
	String type(){
		return "models";
	}

	/**
	 * @see Model#getProperty()
	 */
	public Collection<Model> getByProperty(final Property property){
		ContainerFilter<Model> filter = new ContainerFilter<Model>(){

			public boolean accept(Model model){
				return ObjectUtil.equals(model.getProperty(), property);
			}
		};

		return getAll(filter);
	}
}