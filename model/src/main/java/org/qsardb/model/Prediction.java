/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Prediction"
)
@XmlType (
	name = "Prediction"
)
public class Prediction extends Parameter<PredictionRegistry, Prediction> {

	@XmlIDREF
	@XmlElement (
		name = "ModelId",
		required = true
	)
	private Model model = null;

	@XmlElement (
		name = "Type",
		required = true
	)
	private Type type = null;

	@XmlElement (
		name = "Application",
		required = false
	)
	private String application = null;


	@Deprecated
	public Prediction(){
	}

	public Prediction(String id, Model model, Type type){
		super(id);

		setModel(model);
		setType(type);
	}

	public Model getModel(){
		return this.model;
	}

	public void setModel(Model model){
		this.model = model;
	}

	public Type getType(){
		return this.type;
	}

	public void setType(Type type){
		this.type = type;
	}

	public String getApplication(){
		return this.application;
	}

	public void setApplication(String application){
		this.application = application;
	}

	@XmlEnum
	@XmlType (
		name = "PredictionType"
	)
	static
	public enum Type {
		@XmlEnumValue (
			value = "training"
		)
		TRAINING,
		@XmlEnumValue (
			value = "validation"
		)
		VALIDATION,
		@XmlEnumValue (
			value = "testing"
		)
		TESTING,
		;
	}
}