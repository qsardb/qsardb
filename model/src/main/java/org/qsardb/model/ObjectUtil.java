/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

public class ObjectUtil {

	private ObjectUtil(){
	}

	static
	public int hashCode(Object object){
		return (object != null ? object.hashCode() : 0);
	}

	static
	public boolean equals(Object left, Object right){

		if(left == null && right == null){
			return true;
		} else

		if(left != null && right != null && (left).equals(right)){
			return true;
		}

		return false;
	}
}