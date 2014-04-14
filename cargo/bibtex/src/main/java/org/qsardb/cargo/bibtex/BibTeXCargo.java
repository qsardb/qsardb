/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.bibtex;

import java.io.*;

import org.qsardb.model.*;

import org.jbibtex.*;

public class BibTeXCargo extends Cargo<Container> {

	public BibTeXCargo(Descriptor descriptor){
		super(ID, descriptor);
	}

	public BibTeXCargo(Compound compound){
		super(ID, compound);
	}

	public BibTeXCargo(Model model){
		super(ID, model);
	}

	public BibTeXCargo(Prediction prediction){
		super(ID, prediction);
	}

	public BibTeXCargo(Property property){
		super(ID, property);
	}

	@Override
	public boolean isBinary(){
		return false;
	}

	@Override
	public String getMimeType(){
		return "application/x-bibtex";
	}

	public BibTeXDatabase loadBibTeX() throws IOException, QdbException {
		InputStream is = getInputStream();

		try {
			Reader reader = new InputStreamReader(is, "UTF-8");

			try {
				BibTeXParser parser = new BibTeXParser();

				return parser.parse(reader);
			} catch(ParseException pe){
				throw new QdbException(pe);
			} finally {
				reader.close();
			}
		} finally {
			is.close();
		}
	}

	public void storeBibTeX(BibTeXDatabase database) throws IOException {
		OutputStream os = getOutputStream();

		try {
			Writer writer = new OutputStreamWriter(os, "UTF-8");

			try {
				BibTeXFormatter formatter = new BibTeXFormatter();

				formatter.format(database, writer);
			} finally {
				writer.close();
			}
		} finally {
			os.close();
		}
	}

	public static final String ID = "bibtex";
}