/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.storage.mercurial;

public class HgStatus {

	private Code code = null;

	private String path = null;


	HgStatus(){
	}

	public String getCanonicalPath(){
		String path = getPath();

		return path.replace('\\', '/');
	}

	public Code getCode(){
		return this.code;
	}

	void setCode(Code code){
		this.code = code;
	}

	public String getPath(){
		return this.path;
	}

	void setPath(String path){
		this.path = path;
	}

	@Override
	public String toString(){
		return getCode().getValue() + " " + getPath();
	}

	public enum Code {
		MODIFIED("M"),
		ADDED("A"),
		REMOVED("R"),
		CLEAN("C"),
		MISSING("!"),
		NOT_TRACKED("?"),
		IGNORED("I");

		private String value = null;


		Code(String value){
			setValue(value);
		}

		public String getValue(){
			return this.value;
		}

		private void setValue(String value){
			this.value = value;
		}

		static
		public Code forValue(String value){
			Code[] codes = Code.values();
			for(Code code : codes){

				if((code.getValue()).equals(value)){
					return code;
				}
			}

			throw new IllegalArgumentException(value);
		}
	}
}