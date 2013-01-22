/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import java.io.*;
import java.util.*;

import org.qsardb.cargo.map.*;
import org.qsardb.model.*;

public class MatrixUtil {

	private MatrixUtil(){
	}

	static
	double[] loadInterceptValues(List<Compound> compounds){
		double[] result = new double[compounds.size()];
		Arrays.fill(result, 1);

		return result;
	}

	static
	double[] loadValues(List<Compound> compounds, Parameter<?, ?> parameter) throws IOException {
		double[] result = new double[compounds.size()];

		ValuesCargo valuesCargo = parameter.getCargo(ValuesCargo.class);

		Map<String, Double> values = valuesCargo.loadDoubleMap();

		for(int i = 0; i < compounds.size(); i++){
			Compound compound = compounds.get(i);

			Double value = values.get(compound.getId());
			if(value == null || (value.isNaN() || value.isInfinite())){
				throw new IllegalArgumentException(String.valueOf(value));
			}

			result[i] = value.doubleValue();
		}

		return result;
	}

	static
	List<String> toIdList(List<Descriptor> descriptors){
		List<String> ids = new ArrayList<String>();

		for(Descriptor descriptor : descriptors){
			String id = descriptor.getId();

			ids.add(id);
		}

		return ids;
	}

	static
	List<Descriptor> fromIdList(Qdb qdb, List<String> ids){
		List<Descriptor> descriptors = new ArrayList<Descriptor>();

		for(String id : ids){
			Descriptor descriptor = qdb.getDescriptor(id);
			if(descriptor == null){
				throw new IllegalArgumentException(id);
			}

			descriptors.add(descriptor);
		}

		return descriptors;
	}
}