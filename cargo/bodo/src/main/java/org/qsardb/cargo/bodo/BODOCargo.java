/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.cargo.bodo;

import java.beans.*;
import java.io.*;
import java.util.*;

import org.qsardb.model.*;

import net.sf.blueobelisk.*;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.*;
import org.yaml.snakeyaml.introspector.*;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.representer.*;

public class BODOCargo extends Cargo<Descriptor> {

	public BODOCargo(Descriptor descriptor){
		super(ID, descriptor);
	}

	@Override
	public boolean isBinary(){
		return false;
	}

	@Override
	public String getMimeType(){
		return "text/x-yaml";
	}

	public BODODescriptor loadBodoDescriptor() throws IOException {
		TypeDescription descriptorDescription = new TypeDescription(BODODescriptor.class);
		descriptorDescription.putListPropertyType("implementations", BODODescriptor.Implementation.class);

		Yaml yaml = new Yaml(new Constructor(descriptorDescription));

		return (BODODescriptor)yaml.load(loadString());
	}

	public void storeBodoDescriptor(BODODescriptor bodoDescriptor) throws IOException {
		Representer representer = new Representer();
		representer.setPropertyUtils(new PropertyOrderingUtils());

		Yaml yaml = new Yaml(representer);

		storeString(yaml.dump(bodoDescriptor));
	}

	static
	private class PropertyOrderingUtils extends PropertyUtils {

		@Override
		protected Set<Property> createPropertySet(Class<?> clazz, BeanAccess access) throws IntrospectionException {
			List<Property> properties = new ArrayList<Property>(super.createPropertySet(clazz, access));

			// Sort simple properties first and Collection-type properties last
			Comparator<Property> comparator = new Comparator<Property>(){

				public int compare(Property left, Property right){
					int leftScore = score(left.getType());
					int rightScore = score(right.getType());

					return (leftScore - rightScore);
				}

				private int score(Class<?> clazz){

					if((Collection.class).isAssignableFrom(clazz) || (Map.class).isAssignableFrom(clazz)){
						return 1;
					}

					return 0;
				}
			};
			Collections.sort(properties, comparator);

			return new LinkedHashSet<Property>(properties);
		}
	}

	public static final String ID = "bodo";
}