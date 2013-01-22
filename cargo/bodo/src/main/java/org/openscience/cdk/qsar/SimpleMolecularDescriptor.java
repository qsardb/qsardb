/*
 * Copyright (c) 2012 University of Tartu
 */
package org.openscience.cdk.qsar;

import java.util.*;

import org.openscience.cdk.exception.*;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.qsar.result.*;

public class SimpleMolecularDescriptor implements IMolecularDescriptor {

	private IMolecularDescriptor descriptor = null;

	private String name = null;


	public SimpleMolecularDescriptor(IMolecularDescriptor descriptor, String name){
		setDescriptor(descriptor);
		setName(name);

		getIndex();
	}

	@Override
	public DescriptorSpecification getSpecification(){
		return getDescriptor().getSpecification();
	}

	@Override
	public String[] getDescriptorNames(){
		return new String[]{getName()};
	}

	@Override
	public String[] getParameterNames(){
		return getDescriptor().getParameterNames();
	}

	@Override
	public Object getParameterType(String name){
		return getDescriptor().getParameterType(name);
	}

	@Override
	public Object[] getParameters(){
		return getDescriptor().getParameters();
	}

	@Override
	public void setParameters(Object[] parameters) throws CDKException {
		getDescriptor().setParameters(parameters);
	}

	@Override
	public IDescriptorResult getDescriptorResultType(){
		IDescriptorResult result = getDescriptor().getDescriptorResultType();

		if(result instanceof DoubleArrayResultType){
			return new DoubleResultType();
		} else

		if(result instanceof IntegerArrayResultType){
			return new IntegerResultType();
		} else

		{
			throw new IllegalStateException();
		}
	}

	@Override
	public DescriptorValue calculate(IAtomContainer molecule){
		DescriptorValue value = getDescriptor().calculate(molecule);

		int index = getIndex();

		IDescriptorResult result = value.getValue();

		if(result instanceof DoubleArrayResult){
			DoubleArrayResult arrayResult = (DoubleArrayResult)result;

			result = new DoubleResult(arrayResult.get(index));
		} else

		if(result instanceof IntegerArrayResult){
			IntegerArrayResult arrayResult = (IntegerArrayResult)result;

			result = new IntegerResult(arrayResult.get(index));
		} else

		{
			throw new IllegalStateException();
		}

		return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), result, getDescriptorNames());
	}

	public int getIndex(){
		int index = Arrays.asList(getDescriptor().getDescriptorNames()).indexOf(getName());
		if(index < 0){
			throw new IllegalArgumentException();
		}

		return index;
	}

	public IMolecularDescriptor getDescriptor(){
		return this.descriptor;
	}

	private void setDescriptor(IMolecularDescriptor descriptor){
		this.descriptor = descriptor;
	}

	public String getName(){
		return this.name;
	}

	private void setName(String name){
		this.name = name;
	}
}