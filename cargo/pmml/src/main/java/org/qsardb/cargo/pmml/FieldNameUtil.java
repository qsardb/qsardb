/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.pmml;

import org.qsardb.model.*;

import org.jpmml.manager.*;

import org.dmg.pmml.*;

public class FieldNameUtil {

	private FieldNameUtil(){
	}

	static
	public boolean isProperty(FieldName name){
		return isParameterType(TYPE_PROPERTY, name);
	}

	static
	public FieldName encodeProperty(Property property){
		return encodePropertyId(property.getId());
	}

	static
	public FieldName encodePropertyId(String id){
		return encodeParameterId(TYPE_PROPERTY, id);
	}

	static
	public Property decodeProperty(Qdb qdb, FieldName name){
		return qdb.getProperty(decodePropertyId(name));
	}

	static
	public String decodePropertyId(FieldName name){
		return decodeParameterId(TYPE_PROPERTY, name);
	}

	static
	public FieldName addPropertyField(ModelManager<?> manager, Property property){
		return addPropertyField(manager, property, OpType.CONTINUOUS, DataType.DOUBLE);
	}

	static
	public FieldName addPropertyField(ModelManager<?> manager, Property property, OpType opType, DataType dataType){
		FieldName name = FieldNameUtil.encodeProperty(property);

		manager.addField(name, property.getName(), opType, dataType, FieldUsageType.PREDICTED);

		return name;
	}

	static
	public boolean isDescriptor(FieldName name){
		return isParameterType(TYPE_DESCRIPTOR, name);
	}

	static
	public FieldName encodeDescriptor(Descriptor descriptor){
		return encodeDescriptorId(descriptor.getId());
	}

	static
	public FieldName encodeDescriptorId(String id){
		return encodeParameterId(TYPE_DESCRIPTOR, id);
	}

	static
	public Descriptor decodeDescriptor(Qdb qdb, FieldName name){
		return qdb.getDescriptor(decodeDescriptorId(name));
	}

	static
	public String decodeDescriptorId(FieldName name){
		return decodeParameterId(TYPE_DESCRIPTOR, name);
	}

	static
	public FieldName addDescriptorField(ModelManager<?> manager, Descriptor descriptor){
		return addDescriptorField(manager, descriptor, OpType.CONTINUOUS, DataType.DOUBLE);
	}

	static
	public FieldName addDescriptorField(ModelManager<?> manager, Descriptor descriptor, OpType opType, DataType dataType){
		FieldName name = FieldNameUtil.encodeDescriptor(descriptor);

		manager.addField(name, descriptor.getName(), opType, dataType, FieldUsageType.ACTIVE);

		return name;
	}

	static
	private boolean isParameterType(String type, FieldName name){
		String value = name.getValue();

		if(value.indexOf('/') > -1){
			return value.startsWith(type + "/");
		}

		return true;
	}

	static
	private FieldName encodeParameterId(String type, String id){
		FieldName name = new FieldName((type + "/") + id);

		return name;
	}

	static
	private String decodeParameterId(String type, FieldName name){
		String value = name.getValue();

		if(value.indexOf('/') > -1){

			if(!value.startsWith(type + "/")){
				throw new IllegalArgumentException();
			}

			return value.substring((type + "/").length());
		}

		return value;
	}

	public static final String TYPE_DESCRIPTOR = "descriptors";
	public static final String TYPE_PROPERTY = "properties";
}