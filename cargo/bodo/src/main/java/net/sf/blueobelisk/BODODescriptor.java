/*
 * Copyright (c) 2012 University of Tartu
 */
package net.sf.blueobelisk;

import java.util.*;

public class BODODescriptor {

	private String ontologyReference = null;

	private List<Implementation> implementations = null;


	public String getOntologyReference(){
		return this.ontologyReference;
	}

	public void setOntologyReference(String ontologyReference){
		this.ontologyReference = ontologyReference;
	}

	public List<Implementation> getImplementations(){
		return this.implementations;
	}

	public void setImplementations(List<Implementation> implementations){
		this.implementations = implementations;
	}

	static
	public class Implementation {

		private String title = null;

		private String identifier = null;

		private String vendor = null;

		private Map<String, Object> parameters = null;


		public String getTitle(){
			return this.title;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getIdentifier(){
			return this.identifier;
		}

		public void setIdentifier(String identifier){
			this.identifier = identifier;
		}

		public String getVendor(){
			return this.vendor;
		}

		public void setVendor(String vendor){
			this.vendor = vendor;
		}

		public Map<String, Object> getParameters(){
			return this.parameters;
		}

		public void setParameters(Map<String, Object> parameters){
			this.parameters = parameters;
		}
	}
}