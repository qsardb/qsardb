/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.storage.mercurial;

public class HgResult {

	private byte[] output = null;

	private byte[] error = null;


	HgResult(byte[] output, byte[] error){
		setOutput(output);
		setError(error);
	}

	public byte[] getOutput(){
		return this.output;
	}

	private void setOutput(byte[] output){
		this.output = output;
	}

	public byte[] getError(){
		return this.error;
	}

	private void setError(byte[] error){
		this.error = error;
	}
}