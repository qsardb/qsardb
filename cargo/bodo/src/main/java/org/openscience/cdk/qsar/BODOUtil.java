/*
 * Copyright (c) 2012 University of Tartu
 */
package org.openscience.cdk.qsar;

import java.util.*;
import java.util.regex.*;

import net.sf.blueobelisk.*;

public class BODOUtil {

	private BODOUtil(){
	}

	static
	public BODODescriptor format(IDescriptor descriptor) throws Exception {
		BODODescriptor bodoDescriptor = new BODODescriptor();

		DescriptorSpecification specification = descriptor.getSpecification();
		bodoDescriptor.setOntologyReference(specification.getSpecificationReference());

		BODODescriptor.Implementation implementation = formatImplementation(descriptor);

		List<BODODescriptor.Implementation> implementations = Collections.singletonList(implementation);
		bodoDescriptor.setImplementations(implementations);

		return bodoDescriptor;
	}

	static
	private BODODescriptor.Implementation formatImplementation(IDescriptor descriptor){
		BODODescriptor.Implementation implementation = new BODODescriptor.Implementation();

		DescriptorSpecification specification = descriptor.getSpecification();

		implementation.setIdentifier(specification.getImplementationIdentifier());
		implementation.setTitle(specification.getImplementationTitle());
		implementation.setVendor(specification.getImplementationVendor());

		if(descriptor instanceof SimpleMolecularDescriptor){
			SimpleMolecularDescriptor simpleDescriptor = (SimpleMolecularDescriptor)descriptor;

			implementation.setTitle(implementation.getTitle() + "[" + simpleDescriptor.getName() + "]");
		}

		Map<String, Object> parameters = new LinkedHashMap<String, Object>();

		String[] parameterNames = descriptor.getParameterNames();
		if(parameterNames != null){
			Object[] parameterValues = descriptor.getParameters();

			for(int i = 0; i < parameterNames.length; i++){
				parameters.put(parameterNames[i], parameterValues[i]);
			}
		}

		implementation.setParameters(parameters);

		return implementation;
	}

	static
	public IDescriptor parse(BODODescriptor bodoDescriptor) throws Exception {
		List<BODODescriptor.Implementation> implementations = bodoDescriptor.getImplementations();

		for(BODODescriptor.Implementation implementation : implementations){
			String vendor = implementation.getVendor();

			if((vendor).contains("CDK") || (vendor).contains("Chemistry Development Kit")){
				return parseImplementation(implementation);
			}
		}

		throw new IllegalArgumentException(bodoDescriptor.getOntologyReference());
	}

	static
	private IDescriptor parseImplementation(BODODescriptor.Implementation implementation) throws Exception {
		Matcher matcher = BODOUtil.pattern.matcher(implementation.getTitle());
		if(!matcher.matches()){
			throw new IllegalArgumentException();
		}

		Class<?> clazz = Class.forName(matcher.group(1));

		IMolecularDescriptor descriptor = (IMolecularDescriptor)clazz.newInstance();

		String name = matcher.group(2);
		if(name != null){
			descriptor = new SimpleMolecularDescriptor(descriptor, name);
		}

		Map<String, Object> parameters = implementation.getParameters();

		String[] parameterNames = descriptor.getParameterNames();
		if(parameterNames != null){
			Object[] parameterValues = new Object[parameterNames.length];

			for(int i = 0; i < parameterNames.length; i++){
				parameterValues[i] = parameters.get(parameterNames[i]);
			}

			descriptor.setParameters(parameterValues);
		}

		return descriptor;
	}

	private static final Pattern pattern = Pattern.compile("([^\\[]+)(?:\\[(.+)\\])?");
}