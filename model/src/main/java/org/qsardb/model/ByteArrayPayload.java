/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public class ByteArrayPayload implements Payload {

	private byte[] bytes = null;


	public ByteArrayPayload(byte[] bytes){
		this.bytes = bytes;
	}

	public InputStream getInputStream(){
		return new ByteArrayInputStream(this.bytes);
	}

	public static final ByteArrayPayload EMPTY = new ByteArrayPayload(new byte[0]);
}