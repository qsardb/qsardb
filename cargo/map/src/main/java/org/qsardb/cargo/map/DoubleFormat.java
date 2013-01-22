/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

import java.text.*;
import java.util.*;

public class DoubleFormat extends ValueFormat<Double> {

	private DecimalFormat format = null;


	public DoubleFormat(){
	}

	public DoubleFormat(String pattern){
		this(new DecimalFormat(pattern, (DecimalFormatSymbols)DEFAULT_SYMBOLS.clone()));
	}

	/**
	 * @throws IllegalArgumentException If the DecimalFormat uses {@link DecimalFormat#getDecimalFormatSymbols() DecimalFormatSymbols} which has been initialized with a {@link Locale} other than {@link Locale#US}.
	 */
	public DoubleFormat(DecimalFormat format){
		DecimalFormatSymbols formatSymbols = format.getDecimalFormatSymbols();

		if(!(formatSymbols).equals(DEFAULT_SYMBOLS)){
			throw new IllegalArgumentException();
		}

		setFormat(format);
	}

	@Override
	protected boolean isNA(Double value){

		if(value != null){
			return value.isNaN();
		}

		return true;
	}

	@Override
	protected String formatValue(Double value){

		if(this.format != null){
			return this.format.format(value);
		}

		return String.valueOf(value);
	}

	@Override
	protected Double parseNA(){
		return Double.valueOf(Double.NaN);
	}

	/**
	 * @throws NumberFormatException
	 */
	@Override
	protected Double parseString(String string){
		string = filterDecimal(string);

		return Double.valueOf(string);
	}

	@Override
	public int hashCode(){
		return super.hashCode() ^ hashCode(this.format);
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof DoubleFormat){
			DoubleFormat that = (DoubleFormat)object;

			return super.equals(object) && equals(this.format, that.format);
		}

		return false;
	}

	public DecimalFormat getFormat(){
		return this.format;
	}

	private void setFormat(DecimalFormat format){
		this.format = format;
	}

	static
	private int hashCode(DecimalFormat format){
		return (format != null ? format.hashCode() : 0);
	}

	static
	private boolean equals(DecimalFormat left, DecimalFormat right){

		if(left == null || right == null){
			return (left == right);
		}

		return (left).equals(right);
	}

	private static final DecimalFormatSymbols DEFAULT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
}