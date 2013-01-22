/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.query;

abstract
public class BinaryExpression extends Expression {

	private Expression left = null;

	private Expression right = null;


	public BinaryExpression(Expression left, Expression right){
		setLeft(left);
		setRight(right);
	}

	public Expression getLeft(){
		return this.left;
	}

	public void setLeft(Expression left){
		this.left = left;
	}

	public Expression getRight(){
		return this.right;
	}

	public void setRight(Expression right){
		this.right = right;
	}
}