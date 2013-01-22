/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

import java.io.*;
import java.util.*;

import org.qsardb.model.*;

import org.junit.*;

import static org.junit.Assert.*;

public class MapCargoTest {

	private Property property = null;

	private PropertyCargo cargo = null;


	@Test (
		expected = UnsupportedOperationException.class
	)
	public void unmodifiableMap() throws IOException {
		Map<String, String> map = this.cargo.loadStringMap();

		map.put("key", "value");
	}

	@Test
	public void caching() throws IOException {
		Map<String, String> stringMap = this.cargo.loadStringMap();
		Map<String, Boolean> booleanMap = this.cargo.loadBooleanMap();

		assertSame(stringMap, this.cargo.loadStringMap());
		assertSame(booleanMap, this.cargo.loadBooleanMap());

		assertNotSame(stringMap, booleanMap);

		// Modify Cargo
		this.cargo.storeStringMap(EMPTY);

		assertNotSame(stringMap, this.cargo.loadStringMap());
		assertNotSame(booleanMap, this.cargo.loadBooleanMap());

		assertEquals(stringMap, this.cargo.loadStringMap());
		assertEquals(booleanMap, this.cargo.loadBooleanMap());
	}

	@Before
	public void begin() throws IOException {
		this.property = new Property();
		this.cargo = this.property.addCargo(PropertyCargo.class);

		this.cargo.storeStringMap(EMPTY);
	}

	@After
	public void end(){
		this.property = null;
		this.cargo = null;
	}

	private static final Map<String, String> EMPTY = Collections.<String, String>emptyMap();
}