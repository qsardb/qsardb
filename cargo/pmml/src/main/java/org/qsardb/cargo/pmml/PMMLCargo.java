/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.pmml;

import java.io.*;

import javax.xml.bind.*;

import org.qsardb.model.*;
import org.qsardb.model.Model;

import org.dmg.pmml.*;

import org.xml.sax.*;

public class PMMLCargo extends Cargo<Model> {

	protected PMMLCargo(Model model){
		super(PMMLCargo.ID, model);
	}

	@Override
	public boolean isBinary(){
		return false;
	}

	@Override
	public String getMimeType(){
		return "text/xml";
	}

	public PMML loadPmml() throws IOException, QdbException {
		InputStream is = getInputStream();

		try {
			return IOUtil.unmarshal(is);
		} catch(SAXException se){
			throw new QdbException(se);
		} catch(JAXBException je){
			throw new QdbException(je);
		} finally {
			is.close();
		}
	}

	public void storePmml(PMML pmml) throws IOException, QdbException {
		OutputStream os = getOutputStream();

		try {
			IOUtil.marshal(pmml, os);
		} catch(JAXBException je){
			throw new QdbException(je);
		} finally {
			os.close();
		}
	}

	public static final String ID = "pmml";
}