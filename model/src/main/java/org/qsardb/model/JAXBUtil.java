/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.*;

public class JAXBUtil {

	private JAXBUtil(){
	}

	static
	public Marshaller createMarshaller() throws JAXBException {
		return createMarshaller(getJAXBContext());
	}

	static
	public Marshaller createMarshaller(JAXBContext jaxbCtx) throws JAXBException {
		Marshaller marshaller = jaxbCtx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		return marshaller;
	}

	static
	public Unmarshaller createUnmarshaller() throws JAXBException {
		return createUnmarshaller(getJAXBContext());
	}

	static
	public Unmarshaller createUnmarshaller(JAXBContext jaxbCtx) throws JAXBException {
		Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();

		return unmarshaller;
	}

	static
	private JAXBContext getJAXBContext() throws JAXBException {

		if(JAXBUtil.jaxbCtx == null){
			JAXBUtil.jaxbCtx = JAXBContext.newInstance(ObjectFactory.class);
		}

		return JAXBUtil.jaxbCtx;
	}

	private static JAXBContext jaxbCtx = null;
}