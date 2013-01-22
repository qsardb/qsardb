/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.cargo.map;

import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;

import org.qsardb.model.*;

abstract
public class MapCargo<P extends Parameter> extends Cargo<P> {

	private Map<ValueFormat<?>, Map<String, ?>> cache = new HashMap<ValueFormat<?>, Map<String, ?>>();


	protected MapCargo(String id, P parameter){
		super(id, parameter);
	}

	/**
	 * The name of the key header field.
	 */
	abstract
	protected String keyName();

	/**
	 * The name of the value header field.
	 */
	abstract
	protected String valueName();

	@Override
	public boolean isBinary(){
		return false;
	}

	@Override
	public String getMimeType(){
		return "text/tab-separated-values";
	}

	@Override
	protected void close() throws IOException {
		super.close();

		this.cache.clear();
	}

	public Map<String, Boolean> loadBooleanMap() throws IOException {
		return loadMap(new BooleanFormat());
	}

	public Map<String, Double> loadDoubleMap() throws IOException {
		return loadMap(new DoubleFormat());
	}

	public Map<String, Double> loadDoubleMap(DecimalFormat format) throws IOException {
		return loadMap(new DoubleFormat(format));
	}

	public Map<String, BigDecimal> loadBigDecimalMap() throws IOException {
		return loadMap(new BigDecimalFormat());
	}

	public Map<String, String> loadStringMap() throws IOException {
		return loadMap(new StringFormat());
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	public <V> Map<String, V> loadMap(ValueFormat<V> parser) throws IOException {
		Map<String, V> cachedMap = (Map<String, V>)this.cache.get(parser);

		if(cachedMap != null){
			return cachedMap;
		}

		Map<String, V> map = new LinkedHashMap<String, V>();

		InputStream is = getInputStream();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			for(int i = 0; true; i++){
				String line = reader.readLine();

				if(line == null){
					break;
				}

				int delim = line.indexOf('\t');

				String key = line.substring(0, delim);
				String value = line.substring(delim + 1);

				// Skip header record
				if(i == 0 && isHeader(key, value)){
					continue;
				} // End if

				if(map.containsKey(key)){
					throw new IllegalArgumentException("Duplicate key \'" + key + "\'");
				}

				map.put(key, parser.parse(value));
			}
		} finally {
			is.close();
		}

		map = Collections.unmodifiableMap(map);

		this.cache.put(parser, map);

		return map;
	}

	private boolean isHeader(String key, String value){

		if(key.equalsIgnoreCase(keyName()) || value.equalsIgnoreCase(valueName())){
			return true;
		} else

		if(key.equalsIgnoreCase("Id") || (key.length() > 2 && (key.substring(key.length() - 2)).equalsIgnoreCase("Id"))){
			return true;
		} // End if

		Qdb qdb = getQdb();

		if(qdb != null && (qdb.getCompound(key)) == null){
			return true;
		}

		return false;
	}

	public void storeBooleanMap(Map<String, Boolean> map) throws IOException {
		storeMap(map, new BooleanFormat());
	}

	public void storeDoubleMap(Map<String, Double> map) throws IOException {
		storeMap(map, new DoubleFormat());
	}

	public void storeDoubleMap(Map<String, Double> map, DecimalFormat format) throws IOException {
		storeMap(map, new DoubleFormat(format));
	}

	public void storeBigDecimalMap(Map<String, BigDecimal> map) throws IOException {
		storeMap(map, new BigDecimalFormat());
	}

	public void storeStringMap(Map<String, String> map) throws IOException {
		storeMap(map, new StringFormat());
	}

	public <V> void storeMap(Map<String, V> map, ValueFormat<V> formatter) throws IOException {
		OutputStream os = getOutputStream();

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

			String sep = "";

			// header record
			if(valueName() != null){
				writer.write(keyName());
				writer.write('\t');
				writer.write(valueName());

				sep = "\n";
			}

			for(Map.Entry<String, V> entry : map.entrySet()){
				writer.write(sep);

				String key = entry.getKey();
				String value = formatter.format(entry.getValue());

				// data record
				writer.write(key);
				writer.write('\t');
				writer.write(value);

				sep = "\n";
			}

			writer.flush();
		} finally {
			os.close();
		}

		this.cache.clear();
	}
}