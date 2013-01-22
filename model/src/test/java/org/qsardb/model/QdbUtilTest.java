/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

import org.junit.*;

import static org.junit.Assert.*;

public class QdbUtilTest {

	@Test
	public void validateId(){
		assertFalse(QdbUtil.validateId(""));

		assertTrue(QdbUtil.validateId("id"));
		assertTrue(QdbUtil.validateId("id.sub_id"));

		assertFalse(QdbUtil.validateId("id/sub_id"));
		assertFalse(QdbUtil.validateId("id\\sub_id"));
		assertFalse(QdbUtil.validateId("id:sub_id"));

		assertFalse(QdbUtil.validateId("."));
		assertFalse(QdbUtil.validateId(".."));
	}
}