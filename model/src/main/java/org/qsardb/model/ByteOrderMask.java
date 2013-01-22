/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

public enum ByteOrderMask {
	UTF_8(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF}, "UTF-8"),
	UTF_16LE(new byte[]{(byte)0xFF, (byte)0xFE}, "UTF-16LE"),
	UTF_16BE(new byte[]{(byte)0xFE, (byte)0xFF}, "UTF-16BE"),
	;

	private byte[] bytes = null;

	private String encoding = null;


	ByteOrderMask(byte[] bytes, String encoding){
		setBytes(bytes);
		setEncoding(encoding);
	}

	public byte[] getBytes(){
		return this.bytes;
	}

	private void setBytes(byte[] bytes){
		this.bytes = bytes;
	}

	public String getEncoding(){
		return this.encoding;
	}

	private void setEncoding(String encoding){
		this.encoding = encoding;
	}

	public byte[] prependTo(byte[] bytes){
		byte[] result = new byte[this.bytes.length + bytes.length];

		System.arraycopy(this.bytes, 0, result, 0, this.bytes.length);
		System.arraycopy(bytes, 0, result, this.bytes.length, bytes.length);

		return result;
	}

	static
	public ByteOrderMask valueOf(byte[] bytes){
		return valueOf(bytes, 0, bytes.length);
	}

	static
	public ByteOrderMask valueOf(byte[] bytes, int offset, int length){
		ByteOrderMask[] values = ByteOrderMask.values();

		values:
		for(ByteOrderMask value : values){

			if(value.bytes.length <= length){

				for(int i = 0; i < value.bytes.length; i++){

					if(value.bytes[i] != bytes[offset + i]){
						continue values;
					}
				}

				return value;
			}
		}

		return null;
	}
}