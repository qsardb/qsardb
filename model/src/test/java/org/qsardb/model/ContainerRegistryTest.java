/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.util.*;

import org.qsardb.storage.memory.*;

import org.junit.*;

import static org.junit.Assert.*;

public class ContainerRegistryTest {

	private Qdb qdb;

	private CompoundRegistry compounds;


	@Test
	public void containerLifecycle() throws Exception {
		Compound compound = new Compound("1");

		assertNull(compound.getRegistry());
		assertEquals(State.UNKNOWN, compound.getState());

		this.compounds.add(compound);

		assertNotNull(compound.getRegistry());
		assertEquals(State.ADDED, compound.getState());

		this.qdb.storeChanges();

		assertNotNull(compound.getRegistry());
		assertEquals(State.NORMAL, compound.getState());

		this.compounds.remove(compound);

		assertNotNull(compound.getRegistry());
		assertEquals(State.REMOVED, compound.getState());

		this.qdb.storeChanges();

		assertNull(compound.getRegistry());
		assertEquals(State.UNKNOWN, compound.getState());
	}

	@Test
	public void defaultOrdering(){
		List<Compound> compoundsTemplate = randomCompounds(1, 100);

		this.compounds.addAll(compoundsTemplate);

		Iterator<Compound> it = this.compounds.iterator();

		for(Compound compound : compoundsTemplate){
			assertTrue(it.hasNext() && (compound).equals(it.next()));
		}

		assertFalse(it.hasNext());
	}

	@Test
	public void sortBefore(){
		this.compounds.setOrdering(ORDERING);

		this.compounds.addAll(randomCompounds(1, 10));

		checkOrdering(1, 10);
	}

	@Test
	public void sortInbetween(){
		this.compounds.addAll(randomCompounds(1, 10));

		this.compounds.setOrdering(ORDERING);

		this.compounds.addAll(randomCompounds(11, 20));

		checkOrdering(1, 20);
	}

	@Test
	public void sortAfter(){
		this.compounds.addAll(randomCompounds(1, 10));

		this.compounds.setOrdering(ORDERING);

		checkOrdering(1, 10);
	}

	private void checkOrdering(int min, int max){
		Iterator<Compound> it = this.compounds.iterator();

		for(int i = min; i <= max; i++){
			assertEquals(String.valueOf(i), (it.next()).getId());
		}
	}

	@Before
	public void begin() throws Exception {
		this.qdb = new Qdb(new SimpleMemoryStorage());

		this.compounds = this.qdb.getCompoundRegistry();
	}

	@After
	public void end() throws Exception {
		this.qdb.close();
		this.qdb = null;

		this.compounds = null;
	}

	static
	private List<Compound> randomCompounds(int min, int max){
		List<Compound> compounds = new ArrayList<Compound>();

		for(int i = min; i <= max; i++){
			Compound compound = new Compound(String.valueOf(i));

			compounds.add(compound);
		}

		Collections.shuffle(compounds);

		return compounds;
	}

	private static Comparator<Compound> ORDERING = new Comparator<Compound>(){

		public int compare(Compound left, Compound right){
			return Integer.parseInt(left.getId()) - Integer.parseInt(right.getId());
		}
	};
}