/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

public interface ContainerFilter<C extends Container<?, C>> {

	boolean accept(C container);
}