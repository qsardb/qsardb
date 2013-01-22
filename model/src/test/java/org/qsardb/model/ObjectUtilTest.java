/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

import org.junit.*;

import static org.junit.Assert.*;

public class ObjectUtilTest {

	@Test
	public void nullHashCode(){
		assertEquals(0, ObjectUtil.hashCode(null));
	}

	@Test
	public void nullEquals(){
		assertTrue(ObjectUtil.equals(this, this));
		assertFalse(ObjectUtil.equals(this, null));
		assertFalse(ObjectUtil.equals(null, this));
		assertTrue(ObjectUtil.equals(null, null));
	}
}