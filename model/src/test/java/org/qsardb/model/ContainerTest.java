/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import org.qsardb.storage.memory.*;

import org.junit.*;

import static org.junit.Assert.*;

public class ContainerTest {

	private Qdb qdb;

	private CompoundRegistry compounds;

	private Compound compound;


	@Test
	public void cargoLifecycle() throws Exception {
		CompoundCargo addedCargo = this.compound.addCargo(CompoundCargo.class);
		addedCargo.storeString("Hello World!");

		assertEquals(State.ADDED, addedCargo.getState());
		assertNotNull(addedCargo.getPayload());

		CompoundCargo normalCargo = this.compound.getCargo(CompoundCargo.class);

		assertSame(addedCargo, normalCargo);
		assertEquals(State.ADDED, normalCargo.getState());

		this.qdb.storeChanges();

		assertEquals(State.NORMAL, normalCargo.getState());
		assertNull(normalCargo.getPayload());

		CompoundCargo removedCargo = this.compound.removeCargo(CompoundCargo.class);

		assertSame(addedCargo, removedCargo);
		assertEquals(State.REMOVED, removedCargo.getState());
		assertNotNull(removedCargo.getPayload());

		this.qdb.storeChanges();

		assertEquals(State.UNKNOWN, removedCargo.getState());
		assertNull(removedCargo.getPayload());
	}

	@Test
	public void cargoByIdAndClass(){
		Cargo<Compound> idCargo = this.compound.addCargo(CompoundCargo.ID);
		CompoundCargo clazzCargo = this.compound.getCargo(CompoundCargo.class);

		assertNotSame(idCargo, clazzCargo);
	}

	@Test
	public void cargoByClassAndId(){
		CompoundCargo clazzCargo = this.compound.addCargo(CompoundCargo.class);
		Cargo<Compound> idCargo = this.compound.getCargo(CompoundCargo.ID);

		assertSame(clazzCargo, idCargo);
	}

	@Before
	public void begin() throws Exception {
		this.qdb = new Qdb(new SimpleMemoryStorage());

		this.compounds = this.qdb.getCompoundRegistry();

		this.compound = new Compound("1");

		this.compounds.add(this.compound);
	}

	@After
	public void end() throws Exception {
		this.qdb.close();
		this.qdb = null;

		this.compounds = null;

		this.compound = null;
	}
}