/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.math.*;
import java.util.*;

public class ScaleFrequencyMap extends FrequencyMap<Integer> {

	/**
	 * @see java.text.DecimalFormat
	 */
	public String getPattern(){
		return getPattern(-1);
	}

	/**
	 * @see java.text.DecimalFormat
	 */
	public String getPattern(int minCount){
		int min = minScale(minCount);
		int max = maxScale(minCount);

		StringBuffer sb = new StringBuffer();
		sb.append('0');

		int diff = (max - min);

		if(min > 0 || (diff > 0 && diff <= 4)){
			sb.append('.');

			max = Math.min(min + 4, max);

			for(int i = 1; true; i++){

				if(i <= min){
					sb.append('0');
				} else

				if(i <= max){
					sb.append('#');
				} else

				{
					break;
				}
			}
		}

		return sb.toString();
	}

	public int getCount(int scale){
		return super.getCount(Integer.valueOf(scale));
	}

	public int add(BigDecimal decimal){
		return super.add(Integer.valueOf(decimal.scale()));
	}

	public int remove(BigDecimal decimal){
		return super.remove(Integer.valueOf(decimal.scale()));
	}

	public int minScale(){
		return minScale(-1);
	}

	public int minScale(int minCount){
		Set<Integer> scales = getScales(minCount);
		if(scales.isEmpty()){
			return 0;
		}

		Integer scale = Collections.min(scales);

		return scale.intValue();
	}

	public int maxScale(){
		return maxScale(-1);
	}

	public int maxScale(int minCount){
		Set<Integer> scales = getScales(minCount);
		if(scales.isEmpty()){
			return 0;
		}

		Integer scale = Collections.max(scales);

		return scale.intValue();
	}

	private Set<Integer> getScales(int minCount){
		Set<Integer> keys = getKeys();
		if(minCount < 0){
			return keys;
		}

		Set<Integer> result = new LinkedHashSet<Integer>();

		for(Integer key : keys){
			int count = getCount(key);

			if(count >= minCount){
				result.add(key);
			}
		}

		return result;
	}

	static
	public ScaleFrequencyMap sample(Collection<?> values){
		ScaleFrequencyMap result = new ScaleFrequencyMap();

		for(Object value : values){

			if(value == null){
				continue;
			}

			result.add(BigDecimalFormat.toBigDecimal(value));
		}

		return result;
	}
}