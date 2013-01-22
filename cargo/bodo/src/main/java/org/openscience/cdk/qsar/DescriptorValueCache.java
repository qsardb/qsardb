/*
 * Copyright (c) 2012 University of Tartu
 */
package org.openscience.cdk.qsar;

import java.util.*;

import org.openscience.cdk.exception.*;
import org.openscience.cdk.interfaces.*;

public class DescriptorValueCache extends LinkedHashMap<DescriptorValueCache.Key, DescriptorValue> {

	public Object calculate(IMolecularDescriptor descriptor, IAtomContainer molecule) throws CDKException {

		if(descriptor instanceof SimpleMolecularDescriptor){
			SimpleMolecularDescriptor simpleDescriptor = (SimpleMolecularDescriptor)descriptor;

			Key key = new Key(simpleDescriptor.getDescriptor());

			DescriptorValue value = get(key);
			if(value == null){
				value = (simpleDescriptor.getDescriptor()).calculate(molecule);

				put(key, value);
			}

			return DescriptorUtil.toObject(value, simpleDescriptor.getIndex());
		} else

		{
			Key key = new Key(descriptor);

			DescriptorValue value = get(key);
			if(value == null){
				value = descriptor.calculate(molecule);

				put(key, value);
			}

			return DescriptorUtil.toObject(value);
		}
	}

	static
	public class Key {

		private Class<? extends IDescriptor> clazz = null;

		private Object[] parameters = null;


		private Key(IDescriptor descriptor){
			setClazz(descriptor.getClass());
			setParameters(descriptor.getParameters());
		}

		@Override
		public int hashCode(){
			return getClazz().hashCode() ^ Arrays.hashCode(getParameters());
		}

		@Override
		public boolean equals(Object object){

			if(object instanceof Key){
				Key that = (Key)object;

				return (this.getClazz()).equals(that.getClazz()) && Arrays.equals(this.getParameters(), that.getParameters());
			}

			return false;
		}

		public Class<? extends IDescriptor> getClazz(){
			return this.clazz;
		}

		private void setClazz(Class<? extends IDescriptor> clazz){
			this.clazz = clazz;
		}

		public Object[] getParameters(){
			return this.parameters;
		}

		private void setParameters(Object[] parameters){
			this.parameters = parameters;
		}
	}
}