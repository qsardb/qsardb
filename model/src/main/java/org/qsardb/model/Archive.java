/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Archive"
)
@XmlType (
	name = "Archive"
)
public class Archive implements Resource {

	@XmlElement (
		name = "Name",
		required = false
	)
	private String name = null;

	@XmlElement (
		name = "Description",
		required = false
	)
	private String description = null;

	@XmlTransient
	private Qdb qdb = null;

	@XmlTransient
	private boolean initialized = false;


	Archive(){
	}

	Archive(Qdb qdb){
		setQdb(qdb);
	}

	final
	public String qdbPath(){
		return "archive.xml";
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public Qdb getQdb(){
		return this.qdb;
	}

	void setQdb(Qdb qdb){
		this.qdb = qdb;
	}

	void initialize(){
		this.initialized = true;
	}

	Archive load(Storage storage) throws IOException, QdbException {
		Archive archive = load(this, storage);
		archive.setQdb(getQdb());

		return archive;
	}

	public void storeChanges() throws IOException, QdbException {
		Qdb qdb = getQdb();

		if(!this.initialized){
			(qdb.getStorage()).add(qdbPath());
		}

		store(this, qdb.getStorage());

		this.initialized = true;
	}

	void store(Storage storage) throws IOException, QdbException {
		store(this, storage);
	}

	static
	private Archive load(Archive archive, Storage storage) throws IOException, QdbException {

		try {
			InputStream is = storage.getInputStream(archive.qdbPath());

			Object result;

			try {
				result = (JAXBUtil.createUnmarshaller()).unmarshal(is);
			} finally {
				is.close();
			}

			archive = (Archive)result;
			archive.initialize();
		} catch(FileNotFoundException fnfe){
			// Ignored
		} catch(JAXBException je){
			throw new QdbException(je);
		}

		return archive;
	}

	static
	private void store(Archive archive, Storage storage) throws IOException, QdbException {
		OutputStream os = storage.getOutputStream(archive.qdbPath());

		try {
			(JAXBUtil.createMarshaller()).marshal(archive, os);
		} catch(JAXBException je){
			throw new QdbException(je);
		} finally {
			os.close();
		}
	}
}