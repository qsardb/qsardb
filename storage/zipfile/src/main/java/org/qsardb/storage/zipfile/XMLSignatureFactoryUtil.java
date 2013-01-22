/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.security.*;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;

class XMLSignatureFactoryUtil {

	private XMLSignatureFactoryUtil(){
	}

	/**
	 * @throws NoSuchMechanismException
	 */
	static
	XMLSignatureFactory getDOMInstance(){

		try {
			return XMLSignatureFactory.getInstance("DOM");
		} catch(NoSuchMechanismException nsme){
			Provider provider;

			try {
				Class<?> clazz = Class.forName("org.jcp.xml.dsig.internal.dom.XMLDSigRI");

				provider = (Provider)clazz.newInstance();
			} catch(Exception e){
				throw new NoSuchMechanismException(e);
			}

			return XMLSignatureFactory.getInstance("DOM", provider);
		}
	}
}