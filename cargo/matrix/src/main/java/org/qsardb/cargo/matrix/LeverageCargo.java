/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

import org.ejml.data.*;
import org.ejml.simple.*;

public class LeverageCargo extends MatrixCargo {

	protected LeverageCargo(Model model){
		super(LeverageCargo.ID, model);
	}

	@Override
	public Map<Compound, Double> prepare(List<Compound> compounds, List<Descriptor> descriptors) throws IOException {
		SimpleMatrix x = createMatrix(compounds, descriptors);
		SimpleMatrix xTrans = x.transpose();

		SimpleMatrix matrix = (xTrans.mult(x)).invert();

		Payload payload = new Payload();
		payload.setDescriptorIds(MatrixUtil.toIdList(descriptors));
		payload.setMatrix(matrix.getMatrix());

		storeObject(payload);

		SimpleMatrix H = x.mult(matrix).mult(xTrans);

		return getLeverages(compounds, H);
	}

	@Override
	public Double getCriticalValue(List<Compound> compounds) throws IOException, ClassNotFoundException {
		Payload payload = (Payload)loadObject();

		List<Descriptor> descriptors = MatrixUtil.fromIdList(getQdb(), payload.getDescriptorIds());

		return Double.valueOf(3D * (descriptors.size() + 1D) / compounds.size());
	}

	@Override
	public Map<Compound, Double> predict(List<Compound> compounds) throws IOException, ClassNotFoundException {
		Payload payload = (Payload)loadObject();

		List<Descriptor> descriptors = MatrixUtil.fromIdList(getQdb(), payload.getDescriptorIds());

		SimpleMatrix x = createMatrix(compounds, descriptors);
		SimpleMatrix xTrans = x.transpose();

		SimpleMatrix matrix = SimpleMatrix.wrap(payload.getMatrix());

		SimpleMatrix H = x.mult(matrix).mult(xTrans);

		return getLeverages(compounds, H);
	}

	static
	private Map<Compound, Double> getLeverages(List<Compound> compounds, SimpleMatrix H){
		Map<Compound, Double> result = new LinkedHashMap<Compound, Double>();

		for(int i = 0; i < compounds.size(); i++){
			result.put(compounds.get(i), Double.valueOf(H.get(i, i)));
		}

		return result;
	}

	static
	private SimpleMatrix createMatrix(List<Compound> compounds, List<Descriptor> descriptors) throws IOException {
		SimpleMatrix result = new SimpleMatrix(compounds.size(), 1 + descriptors.size());

		result.setColumn(0, 0, MatrixUtil.loadInterceptValues(compounds));

		for(int i = 0; i < descriptors.size(); i++){
			result.setColumn(1 + i, 0, MatrixUtil.loadValues(compounds, descriptors.get(i)));
		}

		return result;
	}

	static
	private class Payload implements Serializable {

		private List<String> descriptorIds = null;

		private DenseMatrix64F matrix = null;


		public List<String> getDescriptorIds(){
			return this.descriptorIds;
		}

		public void setDescriptorIds(List<String> descriptorIds){
			this.descriptorIds = descriptorIds;
		}

		public DenseMatrix64F getMatrix(){
			return this.matrix;
		}

		public void setMatrix(DenseMatrix64F matrix){
			this.matrix = matrix;
		}
	}

	// XXX
	public static final String ID = LeverageCargo.class.getName();
}