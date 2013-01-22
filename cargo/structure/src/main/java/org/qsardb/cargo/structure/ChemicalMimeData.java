/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.structure;

/**
 * Enumeration of chemical mime types for the representation of chemical
 * structures in Qdb archives. The following code snippets illustrate storing
 * and loading chemical structure data that is represented in CML format:
 *
 * <p><pre>
 * Cargo<Compound> cmlCargo = compound.addCargo(ChemicalMimeData.CML.getId());
 * // use cmlCargo.storeString or cmlCargo.storeByteArray methods for storing
 * // data in CML representation.
 *
 * ...
 *
 * cmlCargo = compound.getCargo(ChemicalMimeData.CML.getId());
 * // use cmlCargo.loadString or cmlCargo.loadByteArray methods for loading.
 * </pre>
 *
 * <p>See http://chemical-mime.sourceforge.net/chemical-mime-data.html
 */
public enum ChemicalMimeData {
	CML("cml"),
	DAYLIGHT_SMILES("daylight-smiles"),
	GAMESS_INPUT("gamess-input"),
	GAMESS_OUTPUT("gamess-output"),
	GAUSSIAN_INPUT("gaussian-input"),
	GAUSSIAN_LOG("gaussian-log"),
	HIN("hin"),
	INCHI("inchi"),
	MDL_MOLFILE("mdl-molfile"),
	MDL_SDFILE("mdl-sdfile"),
	MOL2("mol2"),
	MOPAC_INPUT("mopac-input"),
	MOPAC_OUT("mopac-out"),
	PDB("pdb"),
	;

	private String id = null;


	ChemicalMimeData(String id){
		setId(id);
	}

	public String getId(){
		return this.id;
	}

	private void setId(String id){
		this.id = id;
	}

	public String getMimeType(){
		return "chemical/x-" + getId();
	}
}
