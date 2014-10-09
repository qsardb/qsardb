/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.xml.bind.annotation.*;

@XmlType (
	name = "Container"
)
abstract
public class Container<R extends ContainerRegistry<R, C>, C extends Container<R, C>> implements Stateable, Resource {

	@XmlID
	@XmlElement (
		name = "Id",
		required = true
	)
	private String id = null;

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

	@XmlElement (
		name = "Labels",
		required = false
	)
	@XmlList
	private Set<String> labels = new LinkedHashSet<String>();

	@XmlElement (
		name = "Cargos",
		required = false
	)
	@XmlList
	private Set<String> cargos = new LinkedHashSet<String>();

	@XmlTransient
	private Map<String, Cargo<C>> cargoMap = new HashMap<String, Cargo<C>>();

	@XmlTransient
	private R registry = null;

	@XmlTransient
	private State state = State.UNKNOWN;


	Container(){
	}

	public Container(String id){
		setId(id);
	}

	/**
	 * @throws IllegalStateException If the Container is in unknown state.
	 */
	final
	public String qdbPath(){

		if(getState().equals(State.UNKNOWN)){
			throw new IllegalStateException("The Container is in unknown state");
		}

		R registry = getRegistry();

		return registry.type() + "/" + getId();
	}

	public String getId(){
		return this.id;
	}

	/**
	 * @throws IllegalStateException If the Container is already registered with a Container registry.
	 * @throws IllegalArgumentException If the identifier is not valid.
	 */
	public void setId(String id){

		if(getRegistry() != null){
			throw new IllegalStateException("The Container is already registered with a Container registry");
		} // End if

		if(id != null && !IdUtil.validate(id)){
			throw new IllegalArgumentException("The identifier \"" + id + "\" is not valid");
		}

		this.id = id;
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

	public Set<String> getLabels(){
		return this.labels;
	}

	/**
	 * @return Set of {@link Cargo#getId() Cargo identifiers}.
	 */
	public Set<String> getCargos(){
		return this.cargos;
	}

	public R getRegistry(){
		return this.registry;
	}

	void setRegistry(R registry){
		this.registry = registry;
	}

	public State getState(){
		return this.state;
	}

	void setState(State state){
		this.state = state;
	}

	public Qdb getQdb(){
		R registry = getRegistry();

		if(registry == null){
			return null;
		}

		return registry.getQdb();
	}

	public boolean hasCargo(String id){
		return hasCargo(createCargo(id));
	}

	public <X extends Cargo> boolean hasCargo(Class<? extends X> clazz){
		return hasCargo(createCargo(clazz));
	}

	private boolean hasCargo(Cargo<C> cargo){
		return this.cargos.contains(cargo.getId());
	}

	/**
	 * @throws IllegalArgumentException If the Cargo already exists.
	 * @see #hasCargo(String)
	 */
	public Cargo<C> addCargo(String id){
		return addCargo(createCargo(id));
	}

	/**
	 * @throws IllegalArgumentException If the Cargo already exists.
	 * @see #hasCargo(Class)
	 */
	public <X extends Cargo> X addCargo(Class<? extends X> clazz){
		Cargo<C> result = addCargo(createCargo(clazz));

		return clazz.cast(result);
	}

	private Cargo<C> addCargo(Cargo<C> cargo){

		if(hasCargo(cargo)){
			throw new IllegalArgumentException("Cargo \"" + cargo.getId() + "\" already exists");
		}

		this.cargos.add(cargo.getId());

		cargo.setState(State.ADDED);
		cargo.setPayload(new NullPayload());

		this.cargoMap.put(cargo.getId(), cargo);

		return cargo;
	}

	/**
	 * @throws IllegalArgumentException If the Cargo does not exist.
	 * @see #hasCargo(String)
	 */
	public Cargo<C> getCargo(String id){
		return getCargo(createCargo(id));
	}

	/**
	 * @throws IllegalArgumentException If the Cargo does not exist.
	 * @see #hasCargo(Class)
	 */
	public <X extends Cargo> X getCargo(Class<? extends X> clazz){
		Cargo<C> result = getCargo(createCargo(clazz));

		if(!clazz.isInstance(result)){
			X cargo = createCargo(clazz);

			cargo.setPayload(result.getPayload());
			cargo.setState(result.getState());

			this.cargoMap.put(cargo.getId(), cargo);

			return cargo;
		}

		return clazz.cast(result);
	}

	private Cargo<C> getCargo(Cargo<C> cargo){

		if(!hasCargo(cargo)){
			throw new IllegalArgumentException("Cargo \"" + cargo.getId() + "\" does not exist");
		}

		Cargo<C> result = this.cargoMap.get(cargo.getId());
		if(result == null){
			cargo.setState(State.NORMAL);

			this.cargoMap.put(cargo.getId(), cargo);

			result = cargo;
		}

		return result;
	}

	public Cargo<C> getOrAddCargo(String id){
		return getOrAddCargo(createCargo(id));
	}

	public <X extends Cargo> X getOrAddCargo(Class<? extends X> clazz){
		Cargo<C> result = getOrAddCargo(createCargo(clazz));

		return clazz.cast(result);
	}

	private Cargo<C> getOrAddCargo(Cargo<C> cargo){

		if(hasCargo(cargo)){
			return getCargo(cargo);
		} else

		{
			return addCargo(cargo);
		}
	}

	/**
	 * @throws IllegalArgumentException If the Cargo does not exist.
	 * @see #hasCargo(String)
	 */
	public Cargo<C> removeCargo(String id){
		return removeCargo(createCargo(id));
	}

	/**
	 * @throws IllegalArgumentException If the Cargo does not exist.
	 * @see #hasCargo(Class)
	 */
	public <X extends Cargo> X removeCargo(Class<? extends X> clazz){
		Cargo<C> result = removeCargo(createCargo(clazz));

		return clazz.cast(result);
	}

	private Cargo<C> removeCargo(Cargo<C> cargo){

		if(!hasCargo(cargo)){
			throw new IllegalArgumentException("Cargo \"" + cargo.getId() + "\" does not exist");
		}

		this.cargos.remove(cargo.getId());

		Cargo<C> result = this.cargoMap.get(cargo.getId());
		if(result == null){
			result = cargo;

			this.cargoMap.put(cargo.getId(), result);
		}

		result.setState(State.REMOVED);
		result.setPayload(new NullPayload());

		return result;
	}

	private void removeAllCargos(){
		Set<String> ids = getCargos();

		for(String id : ids){
			Cargo<C> cargo = getCargo(id);

			cargo.setState(State.REMOVED);
			cargo.setPayload(new NullPayload());
		}

		getCargos().clear();
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	private Cargo<C> createCargo(String id){
		return new Cargo<C>(id, (C)this);
	}

	private <X extends Cargo> X createCargo(Class<? extends X> clazz){

		try {
			Constructor<? extends X> constructor = clazz.getDeclaredConstructor(this.getClass());
			constructor.setAccessible(true);

			return constructor.newInstance(this);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * @throws IllegalStateException If the Container is in unknown state.
	 */
	public void storeChanges() throws IOException {

		if(getState().equals(State.UNKNOWN)){
			throw new IllegalStateException("The Container is in unknown state");
		} // End if

		if(getState().equals(State.REMOVED)){
			removeAllCargos();
		}

		Set<String> removableIds = new HashSet<String>();

		for(Map.Entry<String, Cargo<C>> entry : this.cargoMap.entrySet()){
			String id = entry.getKey();
			Cargo<C> cargo = entry.getValue();

			if((cargo.getState()).equals(State.REMOVED)){
				removableIds.add(id);
			}

			cargo.storeChanges();
		}

		(this.cargoMap.keySet()).removeAll(removableIds);

		switch(getState()){
			case NORMAL:
				break;
			case ADDED:
			case MODIFIED:
				setState(State.NORMAL);
				break;
			case REMOVED:
				setState(State.UNKNOWN);
				setRegistry(null);
				break;
			default:
				break;
		}
	}

	void store(Storage storage) throws IOException {
		Set<String> ids = getCargos();

		for(String id : ids){
			Cargo<C> cargo = getCargo(id);

			cargo.store(storage);
		}
	}

	void close() throws IOException {

		for(Cargo<C> cargo : this.cargoMap.values()){
			cargo.close();
		}

		this.cargoMap.clear();
	}

	@Override
	public int hashCode(){
		return ObjectUtil.hashCode(this.getClass()) ^ ObjectUtil.hashCode(this.getId());
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof Container){
			Container<?, ?> that = (Container<?, ?>)object;

			return ObjectUtil.equals(this.getClass(), that.getClass()) && ObjectUtil.equals(this.getId(), that.getId());
		}

		return false;
	}
}