/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "Compound"
)
@XmlType (
	name = "Compound"
)
public class Compound extends Container<CompoundRegistry, Compound> {

	@XmlElement (
		name = "Cas",
		required = false
	)
	private String cas = null;

	@XmlElement (
		name = "InChI",
		required = false
	)
	private String inChI = null;


	@Deprecated
	public Compound(){
	}

	public Compound(String id){
		super(id);
	}

	public String getCas(){
		return this.cas;
	}

	public void setCas(String cas){
		this.cas = cas;
	}

	public String getInChI(){
		return this.inChI;
	}

	public void setInChI(String inChI){
		this.inChI = inChI;
	}
}