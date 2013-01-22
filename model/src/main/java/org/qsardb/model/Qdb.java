/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

/**
 * <code>Qdb</code> (the abbreviation QDB stands for <b>Q</b>SAR <b>D</b>ata<b>B</b>ank) provides facilities to read from and write to QDB archives.
 *
 * <p>
 * {@link Container Containers} are exposed to application programmer via {@link ContainerRegistry Container registries}:
 * <ul>
 * 	<li>{@link #getCompoundRegistry() Compounds registry}.
 *  <li>{@link #getDescriptorRegistry() Descriptors registry}.
 *  <li>{@link #getModelRegistry() Models registry}.
 *  <li>{@link #getPredictionRegistry() Predictions registry} aka Predicted properties registry.
 *  <li>{@link #getPropertyRegistry() Properties registry}.
 * </ul>
 *
 * <p>
 * Note that the allocated resources should be reclaimed by invoking {@link #close()}.
 */
public class Qdb {

	private Archive archive = new Archive(this);

	private CompoundRegistry compoundRegistry = new CompoundRegistry(this);

	private PropertyRegistry propertyRegistry = new PropertyRegistry(this);

	private DescriptorRegistry descriptorRegistry = new DescriptorRegistry(this);

	private ModelRegistry modelRegistry = new ModelRegistry(this);

	private PredictionRegistry predictionRegistry = new PredictionRegistry(this);

	private Storage storage = null;

	private File tempDir = null;


	public Qdb(Storage storage) throws IOException, QdbException {
		load(storage);

		this.storage = storage;
	}

	public Archive getArchive(){
		return this.archive;
	}

	public Compound getCompound(String id){
		return getCompoundRegistry().get(id);
	}

	public CompoundRegistry getCompoundRegistry(){
		return this.compoundRegistry;
	}

	public Property getProperty(String id){
		return getPropertyRegistry().get(id);
	}

	public PropertyRegistry getPropertyRegistry(){
		return this.propertyRegistry;
	}

	public Descriptor getDescriptor(String id){
		return getDescriptorRegistry().get(id);
	}

	public DescriptorRegistry getDescriptorRegistry(){
		return this.descriptorRegistry;
	}

	public Model getModel(String id){
		return getModelRegistry().get(id);
	}

	public ModelRegistry getModelRegistry(){
		return this.modelRegistry;
	}

	public Prediction getPrediction(String id){
		return getPredictionRegistry().get(id);
	}

	public PredictionRegistry getPredictionRegistry(){
		return this.predictionRegistry;
	}

	Storage getStorage(){
		return this.storage;
	}

	File getTempDirectory(){

		if(this.tempDir == null){
			this.tempDir = FileUtil.createTempDirectory();
		}

		return this.tempDir;
	}

	void load(Storage storage) throws IOException, QdbException {
		this.archive = this.archive.load(storage);

		this.compoundRegistry = this.compoundRegistry.load(storage);
		this.propertyRegistry = this.propertyRegistry.load(storage);
		this.descriptorRegistry = this.descriptorRegistry.load(storage);
		this.modelRegistry = this.modelRegistry.load(storage);
		this.predictionRegistry = this.predictionRegistry.load(storage);
	}

	public void storeChanges() throws IOException, QdbException {
		this.archive.storeChanges();

		this.compoundRegistry.storeChanges();
		this.propertyRegistry.storeChanges();
		this.descriptorRegistry.storeChanges();
		this.modelRegistry.storeChanges();
		this.predictionRegistry.storeChanges();
	}

	public void copyTo(Storage storage) throws IOException, QdbException {
		store(storage);
	}

	void store(Storage storage) throws IOException, QdbException {
		this.archive.store(storage);

		this.compoundRegistry.store(storage);
		this.propertyRegistry.store(storage);
		this.descriptorRegistry.store(storage);
		this.modelRegistry.store(storage);
		this.predictionRegistry.store(storage);
	}

	/**
	 * Frees resources.
	 */
	public void close() throws IOException, QdbException {
		this.compoundRegistry.close();
		this.propertyRegistry.close();
		this.descriptorRegistry.close();
		this.modelRegistry.close();
		this.predictionRegistry.close();

		try {
			this.storage.close();
		} finally {
			this.storage = null;
		}

		try {
			if(this.tempDir != null){
				FileUtil.deleteTempDirectory(this.tempDir);
			}
		} finally {
			this.tempDir = null;
		}
	}
}