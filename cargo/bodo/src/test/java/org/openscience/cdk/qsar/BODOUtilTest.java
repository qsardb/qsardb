/*
 * Copyright (c) 2012 University of Tartu
 */
package org.openscience.cdk.qsar;

import org.junit.*;

import org.openscience.cdk.qsar.descriptors.molecular.*;

import static org.junit.Assert.*;

public class BODOUtilTest {

	@Test
	public void handleDescriptor() throws Exception {
		IDescriptor descriptor = new XLogPDescriptor();

		IDescriptor resultDescriptor = BODOUtil.parse(BODOUtil.format(descriptor));

		assertEquals(descriptor.getClass(), resultDescriptor.getClass());
	}

	@Test
	public void handleSimpleDescriptor() throws Exception {
		IDescriptor descriptor = new ALOGPDescriptor();
		SimpleMolecularDescriptor simpleDescriptor = new SimpleMolecularDescriptor((IMolecularDescriptor)descriptor, "ALogP");

		SimpleMolecularDescriptor resultSimpleDescriptor = (SimpleMolecularDescriptor)BODOUtil.parse(BODOUtil.format(simpleDescriptor));
		IDescriptor resultDescriptor = resultSimpleDescriptor.getDescriptor();

		assertEquals(descriptor.getClass(), resultDescriptor.getClass());
	}
}