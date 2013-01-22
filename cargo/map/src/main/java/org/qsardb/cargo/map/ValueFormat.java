/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

abstract
public class ValueFormat<V> {

	final
	public String format(V value){

		if(isNA(value)){
			return ValueFormat.NA;
		}

		return formatValue(value);
	}

	protected boolean isNA(V value){
		return (value == null);
	}

	abstract
	protected String formatValue(V value);

	final
	public V parse(String string){

		if((ValueFormat.NA).equals(string)){
			return parseNA();
		}

		return parseString(string);
	}

	protected V parseNA(){
		return null;
	}

	abstract
	protected V parseString(String string);

	@Override
	public int hashCode(){
		return this.getClass().hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof ValueFormat){
			ValueFormat<?> that = (ValueFormat<?>)object;

			return (this.getClass()).equals(that.getClass());
		}

		return false;
	}

	static
	protected String filterDecimal(String string){

		if(string != null && string.indexOf('.') < 0){
			return string.replace(',', '.');
		}

		return string;
	}

	public static final String NA = "N/A";
}