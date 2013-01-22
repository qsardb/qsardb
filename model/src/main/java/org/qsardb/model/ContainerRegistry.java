/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

import com.sun.xml.bind.*;

@XmlType (
	name = "ContainerRegistry"
)
abstract
public class ContainerRegistry<R extends ContainerRegistry<R, C>, C extends Container<R, C>> extends AbstractSet<C> implements Resource {

	@XmlTransient
	private Map<String, C> containerMap = new HashMap<String, C>();

	@XmlTransient
	private Comparator<C> ordering = null;

	@XmlTransient
	private Qdb qdb = null;

	@XmlTransient
	private boolean initialized = false;


	ContainerRegistry(){
	}

	ContainerRegistry(Qdb qdb){
		setQdb(qdb);
	}

	abstract
	List<C> getList();

	abstract
	String type();

	final
	public String qdbPath(){
		return type() + "/" + type() + ".xml";
	}

	@Override
	public int size(){
		return getList().size();
	}

	@Override
	public boolean isEmpty(){
		return getList().isEmpty();
	}

	@Override
	public Iterator<C> iterator(){
		return getList().iterator();
	}

	@Override
	@SuppressWarnings (
		value = {"unchecked"}
	)
	public boolean contains(Object object){

		if(object instanceof Container){
			Container<?, ?> container = (Container<?, ?>)object;

			return contains((C)container);
		}

		return false;
	}

	public boolean contains(C container){
		return contains(container.getId());
	}

	private boolean contains(String id){
		return get(id) != null;
	}

	/**
	 * Looks up a Container by its identifier.
	 *
	 * @return The Container instance or <code>null</code>
	 */
	public C get(String id){
		C container = this.containerMap.get(id);

		if(container != null && (container.getState()).equals(State.REMOVED)){
			return null;
		}

		return container;
	}

	/**
	 * Looks up a Collection of Containers by their identifiers.
	 *
	 * The returned Collection preserves the iteration order of the passed-in Collection.
	 *
	 * @return Collection of Containers.
	 */
	public Collection<C> getAll(Collection<String> ids){
		Set<C> list = new LinkedHashSet<C>();

		for(String id : ids){
			C container = get(id);

			if(container != null){
				list.add(container);
			}
		}

		return list;
	}

	public Collection<C> getAll(ContainerFilter<C> filter){
		Set<C> result = new LinkedHashSet<C>();

		for(C container : this){

			if(filter.accept(container)){
				result.add(container);
			}
		}

		return result;
	}

	/**
	 * @throws NullPointerException If the Container itself or its {@link Container#getId() Id attribute} is <code>null</code>.
	 * @throws IllegalStateException If the Container is already registered with a Container registry.
	 * @throws IllegalArgumentException If this Container registry already contains a Container with the same {@link Container#getId() Id attribute}.
	 *
	 * @return <code>true</code> Always.
	 */
	@Override
	@SuppressWarnings (
		value = {"unchecked"}
	)
	public boolean add(C container){

		if(container == null){
			throw new NullPointerException("The Container is null");
		} // End if

		if(container.getId() == null){
			throw new NullPointerException("The identifier of the Container is null");
		} // End if

		if(container.getRegistry() != null){
			throw new IllegalStateException("The Container is already registered with a Container registry");
		}

		if(contains(container)){
			throw new IllegalArgumentException("Duplicate Container \"" + container.getId() + "\"");
		}

		this.containerMap.put(container.getId(), container);

		if(this.ordering != null){
			int index = Collections.binarySearch(getList(), container, this.ordering);

			// Container not found
			if(index < 0){
				index = -(index + 1);
			}

			getList().add(index, container);
		} else {
			getList().add(container);
		}

		container.setState(State.ADDED);
		container.setRegistry((R)this);

		return true;
	}

	@Override
	@SuppressWarnings (
		value = {"unchecked"}
	)
	public boolean remove(Object object){

		if(object instanceof Container){
			Container<?, ?> container = (Container<?, ?>)object;

			return remove((C)container);
		}

		return false;
	}

	public boolean remove(C container){
		return remove(container.getId());
	}

	private boolean remove(String id){
		C container = this.containerMap.get(id);

		if(container != null && getList().remove(container)){
			container.setState(State.REMOVED);

			return true;
		}

		return false;
	}

	@Override
	public void clear(){

		for(C container : this){
			container.setState(State.REMOVED);
		}

		getList().clear();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean retainAll(Collection<?> objects){
		throw new UnsupportedOperationException();
	}

	public Comparator<C> getOrdering(){
		return this.ordering;
	}

	/**
	 * @param ordering New ordering or <code>null</code>
	 */
	public void setOrdering(Comparator<C> ordering){
		this.ordering = ordering;

		if(this.ordering != null){
			Collections.sort(getList(), this.ordering);
		}
	}

	public Qdb getQdb(){
		return this.qdb;
	}

	void setQdb(Qdb qdb){
		this.qdb = qdb;
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	private void initialize(){
		this.containerMap.clear();

		for(C container : this){

			if(contains(container)){
				throw new IllegalArgumentException("Duplicate Container \"" + container.getId() + "\"");
			}

			this.containerMap.put(container.getId(), container);

			container.setState(State.NORMAL);
			container.setRegistry((R)this);
		}

		this.initialized = true;
	}

	void close() throws IOException {

		for(C container : this){
			container.close();
		}
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	R load(Storage storage) throws IOException, QdbException {
		R registry = load((R)this, storage);
		registry.setQdb(getQdb());

		return registry;
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	public void storeChanges() throws IOException, QdbException {
		Qdb qdb = getQdb();

		if(!isEmpty()){

			if(!this.initialized){
				qdb.getStorage().add(qdbPath());
			}

			store((R)this, qdb.getStorage());
		} else {

			if(this.initialized){
				(qdb.getStorage()).remove(qdbPath());
			}
		}

		Set<String> removableIds = new HashSet<String>();

		for(Map.Entry<String, C> entry : this.containerMap.entrySet()){
			String id = entry.getKey();
			C container = entry.getValue();

			if((container.getState()).equals(State.REMOVED)){
				removableIds.add(id);
			}

			container.storeChanges();
		}

		(this.containerMap.keySet()).removeAll(removableIds);

		this.initialized = !isEmpty();
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	void store(Storage storage) throws IOException, QdbException {

		if(!isEmpty()){
			store((R)this, storage);
		} else {
			return;
		}

		for(C container : this){
			container.store(storage);
		}
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	private <R extends ContainerRegistry<R, C>, C extends Container<R, C>> R load(R registry, Storage storage) throws IOException, QdbException {

		try {
			InputStream is = storage.getInputStream(registry.qdbPath());

			Object result;

			try {
				Qdb qdb = registry.getQdb();

				Unmarshaller unmarshaller = JAXBUtil.createUnmarshaller();
				unmarshaller.setProperty(IDResolver.class.getName(), new QdbIDResolver(qdb));

				result = unmarshaller.unmarshal(is);
			} finally {
				is.close();
			}

			registry = (R)(registry.getClass()).cast(result);
			registry.initialize();
		} catch(FileNotFoundException fnfe){
			// Ignored
		} catch(JAXBException je){
			throw new QdbException(je);
		}

		return registry;
	}

	static
	private <R extends ContainerRegistry<R, C>, C extends Container<R, C>> void store(R registry, Storage storage) throws IOException, QdbException {
		OutputStream os = storage.getOutputStream(registry.qdbPath());

		try {
			Marshaller marshaller = JAXBUtil.createMarshaller();

			marshaller.marshal(registry, os);
		} catch(JAXBException je){
			throw new QdbException(je);
		} finally {
			os.close();
		}
	}
}