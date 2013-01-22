/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "PredictionRegistry"
)
@XmlType (
	name = "PredictionRegistry"
)
public class PredictionRegistry extends ParameterRegistry<PredictionRegistry, Prediction> {

	@XmlElementRef (
		type = Prediction.class
	)
	private List<Prediction> predictions = new ArrayList<Prediction>();


	PredictionRegistry(){
	}

	PredictionRegistry(Qdb qdb){
		super(qdb);
	}

	@Override
	List<Prediction> getList(){
		return this.predictions;
	}

	@Override
	String type(){
		return "predictions";
	}

	/**
	 * @see Prediction#getModel()
	 */
	public Collection<Prediction> getByModel(final Model model){
		ContainerFilter<Prediction> filter = new ContainerFilter<Prediction>(){

			public boolean accept(Prediction prediction){
				return ObjectUtil.equals(prediction.getModel(), model);
			}
		};

		return getAll(filter);
	}

	/**
	 * @see Prediction#getModel()
	 * @see Prediction#getType()
	 */
	public Collection<Prediction> getByModelAndType(final Model model, final Prediction.Type type){
		return getByModelAndType(model, EnumSet.of(type));
	}

	public Collection<Prediction> getByModelAndType(final Model model, final EnumSet<Prediction.Type> types){
		ContainerFilter<Prediction> filter = new ContainerFilter<Prediction>(){

			public boolean accept(Prediction prediction){
				return ObjectUtil.equals(prediction.getModel(), model) && types.contains(prediction.getType());
			}
		};

		return getAll(filter);
	}
}