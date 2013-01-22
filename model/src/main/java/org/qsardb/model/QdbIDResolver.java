/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

import java.util.concurrent.*;

import com.sun.xml.bind.*;

public class QdbIDResolver extends IDResolver {

	private Qdb qdb = null;


	public QdbIDResolver(Qdb qdb){
		setQdb(qdb);
	}

	@Override
    public void bind(String id, Object object){
	}

	@SuppressWarnings (
		value = {"rawtypes"}
	)
	@Override
    public Callable<?> resolve(final String id, final Class clazz){
		return new Callable<Container>(){

			public Container call(){
				return resolveContainer(id, clazz);
			}
		};
    }

	private Container<?, ?> resolveContainer(String id, Class<?> clazz){
		Qdb qdb = getQdb();

		if(Compound.class.equals(clazz)){
			return qdb.getCompound(id);
		} else

		if(Property.class.equals(clazz)){
			return qdb.getProperty(id);
		} else

		if(Descriptor.class.equals(clazz)){
			return qdb.getDescriptor(id);
		} else

		if(Model.class.equals(clazz)){
			return qdb.getModel(id);
		} else

		if(Prediction.class.equals(clazz)){
			return qdb.getPrediction(id);
		}

		throw new IllegalArgumentException(clazz.getName());
	}

	public Qdb getQdb(){
		return this.qdb;
	}

	private void setQdb(Qdb qdb){
		this.qdb = qdb;
	}
}