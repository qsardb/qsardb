/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.map;

import java.util.*;

public class FrequencyMap<O> {

	private Map<O, Integer> map = new HashMap<O, Integer>();


	public Set<O> getKeys(){
		return this.map.keySet();
	}

	public int getCount(O observation){
		Integer count = this.map.get(observation);
		if(count == null){
			count = Integer.valueOf(0);
		}

		return count.intValue();
	}

	public int getCountSum(){
		int sum = 0;

		Collection<Integer> counts = this.map.values();
		for(Integer count : counts){
			sum += count.intValue();
		}

		return sum;
	}

	public int add(O observation){
		Integer count = this.map.get(observation);
		if(count != null){
			count = Integer.valueOf(count.intValue() + 1);
		} else

		{
			count = Integer.valueOf(1);
		}

		this.map.put(observation, count);

		return count.intValue();
	}

	public int remove(O observation){
		Integer count = this.map.get(observation);
		if(count != null){
			count = Integer.valueOf(count.intValue() - 1);
		} else

		{
			throw new IllegalStateException();
		} // End if

		if(count.intValue() > 0){
			this.map.put(observation, count);
		} else

		{
			this.map.remove(observation);
		}

		return count.intValue();
	}
}