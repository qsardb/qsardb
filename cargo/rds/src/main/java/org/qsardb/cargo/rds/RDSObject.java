/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.rds;

import java.io.*;
import java.util.*;

import org.rosuda.JRI.*;

public class RDSObject {

	private Rengine engine = null;

	private File file = null;

	private String object = null;


	public RDSObject(Rengine engine, File file){
		setEngine(engine);
		setFile(file);
	}

	public String getPropertyId() throws IOException {
		String object = ensureObject();

		REXP result = eval(object + "$getPropertyId(" + object+ ")");

		switch(result.getType()){
			case REXP.XT_STR:
				return result.asString();
			default:
				throw new IOException(result.toString());
		}
	}

	public List<String> getDescriptorIdList() throws IOException {
		String object = ensureObject();

		REXP result = eval(object + "$getDescriptorIdList(" + object + ")");

		switch(result.getType()){
			case REXP.XT_STR:
			case REXP.XT_ARRAY_STR:
				return Arrays.asList(result.asStringArray());
			default:
				throw new IOException(result.toString());
		}
	}

	public String getSummary() throws IOException {
		String object = ensureObject();

		// Workaround for ONS random forest models in the QsarDB repository
		// that don't have getSummary method in the RDS cargo.
		String summaryScript = "ifelse(exists(\"getSummary\", object), object$getSummary(object), ifelse(exists(\"rfmodel\", object), paste(\"Random forest (\", object$rfmodel$type, \")\", sep=\"\"), \"(Unknown model)\"))";
		REXP result = eval(summaryScript.replaceAll("object", object));

		switch(result.getType()){
			case REXP.XT_STR:
				return result.asString();
			default:
				throw new IOException(result.toString());
		}
	}

	public Object evaluate(List<Object> values) throws IOException {
		String object = ensureObject();

		String objectValues = (object + "_values");

		REXP result;

		assign(objectValues, toREXP(values));

		try {
			result = eval(object + "$evaluate(" + object + ", " + objectValues + ")");
		} finally {
			eval("rm(" + objectValues + ")");
		}

		switch(result.getType()){
			case REXP.XT_DOUBLE:
				return asDouble(result.asDouble());
			case REXP.XT_ARRAY_DOUBLE:
				return asDouble(result.asDoubleArray());
			case REXP.XT_INT:
				return asInteger(result.asInt());
			case REXP.XT_ARRAY_INT:
				return asInteger(result.asIntArray());
			case REXP.XT_STR:
				return asString(result.asString());
			case REXP.XT_ARRAY_STR:
				return asString(result.asStringArray());
			default:
				throw new IOException(result.toString());
		}
	}

	public void clear() throws IOException {

		if(this.object != null){
			eval("rm(" + this.object + ")");

			this.object = null;
		}
	}

	private String ensureObject() throws IOException {

		if(this.object == null){
			String object = "rds_" + System.identityHashCode(this);

			eval(object + " = readRDS(file = \'" + getPath() + "\')");

			this.object = object;
		}

		return this.object;
	}

	private REXP eval(String string) throws IOException {
		Rengine engine = ensureEngine();

		REXP result = engine.eval(string);
		if(result == null){
			throw new IOException();
		}

		return result;
	}

	private void assign(String symbol, REXP expression) throws IOException {
		Rengine engine = ensureEngine();

		engine.assign(symbol, expression);
	}

	private Rengine ensureEngine() throws IOException {
		Rengine engine = getEngine();
		if(engine == null){
			throw new IOException();
		}

		return engine;
	}

	private String getPath(){
		String path = getFile().getAbsolutePath();
		if(path.indexOf('\\') > -1){
			path = path.replace('\\', '/');
		}

		return path;
	}

	public Rengine getEngine(){
		return this.engine;
	}

	private void setEngine(Rengine engine){
		this.engine = engine;
	}

	public File getFile(){
		return this.file;
	}

	private void setFile(File file){
		this.file = file;
	}

	static
	private REXP toREXP(List<Object> values){
		double[] array = new double[values.size()];

		for(int i = 0; i < values.size(); i++){
			Number value = (Number)values.get(i);

			array[i] = value.doubleValue();
		}

		return new REXP(array);
	}

	static
	private Double asDouble(double... values){

		if(values.length != 1){
			throw new IllegalArgumentException();
		}

		return Double.valueOf(values[0]);
	}

	static
	private Integer asInteger(int... values){

		if(values.length != 1){
			throw new IllegalArgumentException();
		}

		return Integer.valueOf(values[0]);
	}

	static
	private String asString(String... values){

		if(values.length != 1){
			throw new IllegalArgumentException();
		}

		return values[0];
	}
}