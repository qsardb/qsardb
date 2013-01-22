/*
 * Copyright (c) 2012 University of Tartu
 */
package org.openscience.cdk.qsar;

import org.openscience.cdk.aromaticity.*;
import org.openscience.cdk.exception.*;
import org.openscience.cdk.graph.*;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.qsar.result.*;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.tools.manipulator.*;

public class DescriptorUtil {

	private DescriptorUtil(){
	}

	static
	public IAtomContainer prepareMolecule(IAtomContainer molecule) throws CDKException {

		if(!ConnectivityChecker.isConnected(molecule)){
			throw new CDKException("The structure is not fully connected");
		}

		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

		CDKHueckelAromaticityDetector.detectAromaticity(molecule);

		CDKHydrogenAdder hydrogenator = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		hydrogenator.addImplicitHydrogens(molecule);

		AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);

		return molecule;
	}

	static
	public Object toObject(DescriptorValue value) throws DescriptorException {

		if(value.getException() != null){
			return null;
		}

		return toObject(value.getValue());
	}

	static
	public Object toObject(IDescriptorResult result) throws DescriptorException {

		if(result instanceof BooleanResult){
			boolean booleanValue = ((BooleanResult)result).booleanValue();

			return Boolean.valueOf(booleanValue);
		} else

		if(result instanceof DoubleResult){
			double doubleValue = ((DoubleResult)result).doubleValue();

			validateValue(doubleValue);

			return Double.valueOf(doubleValue);
		} else

		if(result instanceof IntegerResult){
			int intValue = ((IntegerResult)result).intValue();

			return Integer.valueOf(intValue);
		}

		throw new DescriptorException(String.valueOf(result));
	}

	static
	public Object toObject(DescriptorValue value, int index) throws DescriptorException {

		if(value.getException() != null){
			return null;
		}

		return toObject(value.getValue(), index);
	}

	static
	public Object toObject(IDescriptorResult result, int index) throws DescriptorException {

		if(result instanceof DoubleArrayResult){
			double doubleValue = ((DoubleArrayResult)result).get(index);

			validateValue(doubleValue);

			return Double.valueOf(doubleValue);
		} else

		if(result instanceof IntegerArrayResult){
			int intValue = ((IntegerArrayResult)result).get(index);

			return Integer.valueOf(intValue);
		}

		throw new DescriptorException(String.valueOf(result));
	}

	static
	private void validateValue(double value) throws DescriptorException {

		if(Double.isInfinite(value) || Double.isNaN(value)){
			throw new DescriptorException(String.valueOf(value));
		}
	}
}