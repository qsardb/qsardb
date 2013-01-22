/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.storage.mercurial;

public class HgRevision {

	private String changeset = null;

	private String branch = null;

	private String summary = null;


	HgRevision(){
	}

	public int getRevision(){
		String changeset = getChangeset();

		int colon = changeset.indexOf(':');

		return Integer.parseInt(changeset.substring(0, colon));
	}

	public String getChangeset(){
		return this.changeset;
	}

	void setChangeset(String changeset){
		this.changeset = changeset;
	}

	public String getBranch(){
		return this.branch;
	}

	void setBranch(String branch){
		this.branch = branch;
	}

	public String getSummary(){
		return this.summary;
	}

	void setSummary(String summary){
		this.summary = summary;
	}
}