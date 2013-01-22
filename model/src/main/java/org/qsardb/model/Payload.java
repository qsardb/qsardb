/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

public interface Payload {

	InputStream getInputStream() throws IOException;
}