/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.math.*;

public class BigDecimalFormat extends ValueFormat<BigDecimal> {

	@Override
	public String formatValue(BigDecimal value){
		return value.toString();
	}

	@Override
	public BigDecimal parseString(String string){
		string = filterDecimal(string);

		return new BigDecimal(string);
	}

	@SuppressWarnings (
		value = {"boxing"}
	)
	static
	public BigDecimal toBigDecimal(Object value){

		if(value instanceof BigDecimal){
			return (BigDecimal)value;
		} else

		if(value instanceof BigInteger){
			return new BigDecimal((BigInteger)value);
		} else

		if(value instanceof Double){
			double doubleValue = ((Double)value).doubleValue();

			if(Double.isNaN(doubleValue)){
				return null;
			}

			return BigDecimal.valueOf(doubleValue);
		} else

		if(value instanceof Float){
			float floatValue = ((Float)value).floatValue();

			if(Float.isNaN(floatValue)){
				return null;
			}

			return BigDecimal.valueOf(floatValue);
		} else

		if(value instanceof Long){
			return new BigDecimal((Long)value);
		} else

		if(value instanceof Integer){
			return new BigDecimal((Integer)value);
		} else

		if(value instanceof String){
			return new BigDecimal((String)value);
		}

		throw new IllegalArgumentException();
	}
}