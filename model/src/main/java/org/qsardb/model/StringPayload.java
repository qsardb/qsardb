/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public class StringPayload implements Payload {

	private String string = null;

	private String encoding = null;


	public StringPayload(String string){
		this(string, "UTF-8");
	}

	public StringPayload(String string, String encoding){
		this.string = string;
		this.encoding = encoding;
	}

	public InputStream getInputStream() throws IOException {
		byte[] bytes = this.string.getBytes(this.encoding);

		return new ByteArrayInputStream(bytes);
	}
}