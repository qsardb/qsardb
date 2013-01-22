/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRegistry
@SuppressWarnings (
	value = {"deprecation"}
)
public class ObjectFactory {

	public Archive createArchive(){
		return new Archive();
	}

	public Compound createCompound(){
		return new Compound();
	}

	public CompoundRegistry createCompoundRegistry(){
		return new CompoundRegistry();
	}

	public Descriptor createDescriptor(){
		return new Descriptor();
	}

	public DescriptorRegistry createDescriptorRegistry(){
		return new DescriptorRegistry();
	}

	public Model createModel(){
		return new Model();
	}

	public ModelRegistry createModelRegistry(){
		return new ModelRegistry();
	}

	public Prediction createPrediction(){
		return new Prediction();
	}

	public PredictionRegistry createPredictionRegistry(){
		return new PredictionRegistry();
	}

	public Property createProperty(){
		return new Property();
	}

	public PropertyRegistry createPropertyRegistry(){
		return new PropertyRegistry();
	}
}