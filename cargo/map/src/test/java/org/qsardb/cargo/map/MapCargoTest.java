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

	@Test
	public void storeStringCaching() throws IOException {
		Map<String, String> stringMap1 = this.cargo.loadStringMap();

		this.cargo.storeString("key\tstoreString");
		Map<String, String> stringMap2 = this.cargo.loadStringMap();
		assertNotSame(stringMap1, stringMap2);
		assertEquals("storeString", stringMap2.get("key"));
	}

	@Test
	public void storeByteArrayCaching() throws IOException {
		Map<String, String> stringMap1 = this.cargo.loadStringMap();
		this.cargo.storeByteArray("key\tstoreByteArray".getBytes());
		Map<String, String> stringMap2 = this.cargo.loadStringMap();
		assertNotSame(stringMap1, stringMap2);
		assertEquals("storeByteArray", stringMap2.get("key"));
	}

	@Test
	public void setPayloadCaching() throws IOException {
		Map<String, String> stringMap1 = this.cargo.loadStringMap();
		this.cargo.setPayload(new StringPayload("key\tsetPayload"));
		Map<String, String> stringMap2 = this.cargo.loadStringMap();
		assertNotSame(stringMap1, stringMap2);
		assertEquals("setPayload", stringMap2.get("key"));
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