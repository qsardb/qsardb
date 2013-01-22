/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.math.*;

public class ValueUtil {

	private ValueUtil(){
	}

	static
	public boolean equals(Object left, Object right, int scale){

		if(left == null || right == null){
			return (left == right);
		}

		return equals(left.toString(), right.toString(), scale);
	}

	static
	public boolean equals(String left, String right, int scale){

		if(left == null || right == null){
			return (left == right);
		}

		try {
			return equals(new BigDecimal(left), new BigDecimal(right), scale);
		} catch(Exception e){
			// Ignored
		}

		return (left).equals(right);
	}

	static
	public boolean equals(BigDecimal left, BigDecimal right, int scale){

		if(left == null || right == null){
			return (left == right);
		} // End if

		if(scale <= 0){
			scale = Math.min(left.scale(), right.scale());
		} else

		{
			scale = Math.min(Math.min(left.scale(), right.scale()), scale);
		} // End if

		left = limitScale(left, scale);
		right = limitScale(right, scale);

		return (left).compareTo(right) == 0;
	}

	static
	private BigDecimal limitScale(BigDecimal value, int scale){

		if(value.scale() > scale){
			value = value.setScale(scale, RoundingMode.HALF_UP);
		}

		return value;
	}
}