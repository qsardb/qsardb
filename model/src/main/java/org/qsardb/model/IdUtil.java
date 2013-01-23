/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.model;

import java.util.regex.*;

public class IdUtil {

	private IdUtil(){
	}

	/**
	 * Verifies the correctness of an identifier.
	 *
	 * A valid identifier consists of one or more visible US-ASCII characters,
	 * except for the path delimiter characters '/', '\' and ':'.
	 *
	 * <p>
	 * Additionally, an identifier is considered invalid if it:
	 * <ul>
	 *  <li>Equals to path components &quot;.&quot; or &quot;..&quot;.
	 * </ul>
	 *
	 * @see Container#getId()
	 * @see Cargo#getId()
	 */
	static
	public boolean validate(String id){

		if(id.equals("")){
			return false;
		} else

		if(id.equals(".") || id.equals("..")){
			return false;
		}

		Matcher matcher = IdUtil.pattern.matcher(id);

		return matcher.matches();
	}

	private static Pattern pattern = Pattern.compile("[\\p{Graph}&&[^/\\\\\\:]]+");
}