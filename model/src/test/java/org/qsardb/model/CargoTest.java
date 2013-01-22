/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

import org.junit.*;

import static org.junit.Assert.*;

public class CargoTest {

	private Compound compound;

	private CompoundCargo cargo;


	@Test (
		expected = IOException.class
	)
	public void loadMissingPayload() throws IOException {
		this.cargo.getInputStream();
	}

	@Test
	public void binaryPayload() throws IOException {
		this.cargo.storeByteArray(BINARY_MESSAGE);

		assertArrayEquals(BINARY_MESSAGE, this.cargo.loadByteArray());
	}

	@Test
	public void utf8Payload() throws IOException {
		this.cargo.storeString(TEXT_MESSAGE);

		assertFalse(this.cargo.isBinary());

		assertEquals(TEXT_MESSAGE, this.cargo.loadString());
	}

	@Test
	public void bomUtf8Payload() throws IOException {
		this.cargo.storeByteArray(UTF_8_BINARY_MESSAGE);

		assertFalse(this.cargo.isBinary());

		assertArrayEquals(UTF_8_BINARY_MESSAGE, this.cargo.loadByteArray());
		assertEquals(new String(BINARY_MESSAGE, ByteOrderMask.UTF_8.getEncoding()), this.cargo.loadString());
	}

	@Test
	public void utf16Payload() throws IOException {
		this.cargo.storeString(TEXT_MESSAGE, "UTF-16");

		assertFalse(this.cargo.isBinary());

		assertEquals(TEXT_MESSAGE, this.cargo.loadString("UTF-16"));
	}

	@Test
	public void bomUtf16Payload() throws IOException {
		this.cargo.storeByteArray(UTF_16LE_BINARY_MESSAGE);

		assertFalse(this.cargo.isBinary());

		assertArrayEquals(UTF_16LE_BINARY_MESSAGE, this.cargo.loadByteArray());
		assertEquals(new String(BINARY_MESSAGE, ByteOrderMask.UTF_16LE.getEncoding()), this.cargo.loadString());
	}

	@Before
	public void begin(){
		this.compound = new Compound("1");

		this.cargo = this.compound.addCargo(CompoundCargo.class);
	}

	@After
	public void end(){
		this.compound = null;

		this.cargo = null;
	}

	public static final byte[] BINARY_MESSAGE = {0x0, 0x0, 0x0, 0x0};

	public static final byte[] UTF_8_BINARY_MESSAGE = ByteOrderMask.UTF_8.prependTo(BINARY_MESSAGE);
	public static final byte[] UTF_16LE_BINARY_MESSAGE = ByteOrderMask.UTF_16LE.prependTo(BINARY_MESSAGE);

	public static final String TEXT_MESSAGE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
}