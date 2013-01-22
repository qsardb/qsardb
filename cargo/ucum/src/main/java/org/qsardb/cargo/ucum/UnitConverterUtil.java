/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.cargo.ucum;

import javax.measure.converter.*;
import javax.measure.unit.*;

public class UnitConverterUtil {

	private UnitConverterUtil(){
	}

	static
	public UnitConverter getConverter(Unit<?> from, Unit<?> to) throws ConversionException {

		if(from instanceof TransformedUnit){
			TransformedUnit<?> xFrom = (TransformedUnit<?>)from;

			UnitConverter transform = getTransform(xFrom);
			if(transform instanceof LogConverter){
				Unit<?> fromParent = xFrom.getParentUnit();
				UnitConverter multiplier = UnitConverter.IDENTITY;

				if(!(from).isCompatible(to) && (from.inverse()).isCompatible(to)){
					fromParent = fromParent.inverse();
					multiplier = new MultiplyConverter(-1);
				}

				// Multiplication first, exponetiation next, unit conversion last
				return (getConverter(fromParent, to)).concatenate((transform.inverse()).concatenate(multiplier));
			}
		} // End if

		if(to instanceof TransformedUnit){
			TransformedUnit<?> xTo = (TransformedUnit<?>)to;

			UnitConverter transform = getTransform(xTo);
			if(transform instanceof LogConverter){
				Unit<?> toParent = xTo.getParentUnit();
				UnitConverter multiplier = UnitConverter.IDENTITY;

				if(!(from).isCompatible(to) && (from).isCompatible(to.inverse())){
					toParent = toParent.inverse();
					multiplier = new MultiplyConverter(-1);
				}

				// Unit conversion first, logarithm calculation next, multiplication last
				return multiplier.concatenate(transform.concatenate(getConverter(from, toParent)));
			}
		}

		return from.getConverterToAny(to);
	}

	static
	private UnitConverter getTransform(TransformedUnit<?> unit){
		UnitConverter transform = unit.toParentUnit();

		if(transform instanceof UnitConverter.Compound){
			UnitConverter.Compound compoundTransform = (UnitConverter.Compound)transform;

			return compoundTransform.getRight();
		}

		return transform;
	}
}