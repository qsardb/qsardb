/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

import org.junit.*;

import static org.junit.Assert.*;

public class IdUtilTest {

	@Test
	public void validate(){
		assertFalse(IdUtil.validate(""));

		assertTrue(IdUtil.validate("id"));
		assertTrue(IdUtil.validate("id.sub_id"));

		assertFalse(IdUtil.validate("id/sub_id"));
		assertFalse(IdUtil.validate("id\\sub_id"));
		assertFalse(IdUtil.validate("id:sub_id"));

		assertFalse(IdUtil.validate("."));
		assertFalse(IdUtil.validate(".."));
	}
}