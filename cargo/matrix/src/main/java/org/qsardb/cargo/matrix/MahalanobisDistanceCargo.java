/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.matrix;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

import org.apache.commons.math.*;
import org.apache.commons.math.distribution.*;
import org.apache.commons.math.stat.descriptive.moment.*;

import org.ejml.data.*;
import org.ejml.simple.*;

public class MahalanobisDistanceCargo extends MatrixCargo {

	protected MahalanobisDistanceCargo(Model model){
		super(ID, model);
	}

	@Override
	public Map<Compound, Double> prepare(List<Compound> compounds, List<Descriptor> descriptors) throws IOException {
		SimpleMatrix matrix = createMatrix(compounds, descriptors);

		SimpleMatrix mu = calculateMeanMatrix(matrix);

		SimpleMatrix cov = calculateCovarianceMatrix(matrix, mu);
		SimpleMatrix covInv = cov.invert();

		Payload payload = new Payload();
		payload.setDescriptorIds(MatrixUtil.toIdList(descriptors));
		payload.setMuMatrix(mu.getMatrix());
		payload.setCovInvMatrix(covInv.getMatrix());

		storeObject(payload);

		return getMahalanobisDistances(compounds, matrix, mu, covInv);
	}

	@Override
	public Double getCriticalValue(List<Compound> compounds) throws IOException, ClassNotFoundException {
		Payload payload = (Payload)loadObject();

		List<Descriptor> descriptors = MatrixUtil.fromIdList(getQdb(), payload.getDescriptorIds());

		ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(descriptors.size());

		try {
			return Double.valueOf(Math.sqrt(distribution.inverseCumulativeProbability(0.95)));
		} catch(MathException me){
			return Double.valueOf(Double.NaN); // XXX
		}
	}

	@Override
	public Map<Compound, Double> predict(List<Compound> compounds) throws IOException, ClassNotFoundException {
		Payload payload = (Payload)loadObject();

		List<Descriptor> descriptors = MatrixUtil.fromIdList(getQdb(), payload.getDescriptorIds());

		SimpleMatrix matrix = createMatrix(compounds, descriptors);

		SimpleMatrix mu = SimpleMatrix.wrap(payload.getMuMatrix());

		SimpleMatrix covInv = SimpleMatrix.wrap(payload.getCovInvMatrix());

		return getMahalanobisDistances(compounds, matrix, mu, covInv);
	}

	static
	private Map<Compound, Double> getMahalanobisDistances(List<Compound> compounds, SimpleMatrix matrix, SimpleMatrix mu, SimpleMatrix covInv){
		Map<Compound, Double> result = new LinkedHashMap<Compound, Double>();

		SimpleMatrix matrixTrans = matrix.transpose();

		for(int i = 0; i < compounds.size(); i++){
			SimpleMatrix x = matrixTrans.extractMatrix(0, matrixTrans.numRows(), i, i + 1);

			SimpleMatrix diff = x.minus(mu);

			// MD is a 1x1 matrix
			SimpleMatrix md = (diff.transpose()).mult(covInv).mult(diff);

			result.put(compounds.get(i), Double.valueOf(Math.sqrt(md.get(0, 0))));
		}

		return result;
	}

	static
	private SimpleMatrix createMatrix(List<Compound> compounds, List<Descriptor> descriptors) throws IOException {
		SimpleMatrix result = new SimpleMatrix(compounds.size(), descriptors.size());

		for(int i = 0; i < descriptors.size(); i++){
			result.setColumn(i, 0, MatrixUtil.loadValues(compounds, descriptors.get(i)));
		}

		return result;
	}

	static
	private SimpleMatrix calculateMeanMatrix(SimpleMatrix matrix){
		SimpleMatrix result = new SimpleMatrix(matrix.numCols(), 1);

		for(int i = 0; i < matrix.numCols(); i++){
			Mean mean = new Mean();

			for(int j = 0; j < matrix.numRows(); j++){
				mean.increment(matrix.get(j, i));
			}

			result.set(i, 0, mean.getResult());
		}

		return result;
	}

	static
	private SimpleMatrix calculateCovarianceMatrix(SimpleMatrix matrix, SimpleMatrix mu){
		SimpleMatrix result = new SimpleMatrix(matrix.numCols(), matrix.numCols());

		for(int i = 0; i < matrix.numCols(); i++){

			for(int j = i; j < matrix.numCols(); j++){
				double covariance = covariance(matrix, i, mu.get(i, 0), j, mu.get(j, 0));

				if(i == j){
					result.set(i, j, covariance);
				} else

				{
					result.set(i, j, covariance);
					result.set(j, i, covariance);
				}
			}
		}

		return result;
	}

	static
	private double covariance(SimpleMatrix matrix, int left, double leftMean, int right, double rightMean){
		double sum = 0;

		int count = matrix.numRows();

		for(int i = 0; i < count; i++){
			sum += (matrix.get(i, left) - leftMean) * (matrix.get(i, right) - rightMean);
		}

		return sum / count;
	}

	static
	private class Payload implements Serializable {

		private List<String> descriptorIds = null;

		private DenseMatrix64F muMatrix = null;

		private DenseMatrix64F covInvMatrix = null;


		public List<String> getDescriptorIds(){
			return this.descriptorIds;
		}

		public void setDescriptorIds(List<String> descriptorIds){
			this.descriptorIds = descriptorIds;
		}

		public DenseMatrix64F getMuMatrix(){
			return this.muMatrix;
		}

		public void setMuMatrix(DenseMatrix64F muMatrix){
			this.muMatrix = muMatrix;
		}

		public DenseMatrix64F getCovInvMatrix(){
			return this.covInvMatrix;
		}

		public void setCovInvMatrix(DenseMatrix64F covInvMatrix){
			this.covInvMatrix = covInvMatrix;
		}
	}

	// XXX
	public static final String ID = MahalanobisDistanceCargo.class.getName();
}