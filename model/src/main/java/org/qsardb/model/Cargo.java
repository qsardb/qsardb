/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.model;

import java.io.*;

/**
 * Cargo is a free-format document attachment to a Container.
 *
 * <p>
 * Cargo implementations must define a one-parametric constructor for every supported Container type.
 * The constructors should declare either <code>protected</code> or default access.
 *
 * <p>
 * For example, if a Cargo implementation is pertinent to {@link Parameter}, it should define three one-parametric constructors:
<pre>
	public class MyCargo extends Cargo&lt;Parameter&gt; {
		protected MyCargo(Descriptor descriptor){...}
		protected MyCargo(Property property){...}
		protected MyCargo(Prediction prediction){...}
	}
</pre>
 */
public class Cargo<C extends Container> implements Stateable, Resource {

	private String id = null;

	private C container = null;

	private State state = State.UNKNOWN;

	private Payload payload = null;


	protected Cargo(C container){
		setId(getClass().getName());
		setContainer(container);
	}

	protected Cargo(String id, C container){
		setId(id);
		setContainer(container);
	}

	/**
	 * @throws IllegalStateException If the cargo is in unknown state.
	 */
	final
	public String qdbPath(){

		if(getState().equals(State.UNKNOWN)){
			throw new IllegalStateException("The Cargo is in unknown state");
		}

		C container = getContainer();

		return container.qdbPath() + "/" +getId();
	}

	public String getId(){
		return this.id;
	}

	/**
	 * @throws NullPointerException If the identifier is <code>null</code>.
	 * @throws IllegalArgumentException If the identifier is not valid.
	 */
	private void setId(String id){

		if(id == null){
			throw new NullPointerException("The identifier is null");
		} // End if

		if(id != null && !IdUtil.validate(id)){
			throw new IllegalArgumentException("The identifier \"" + id + "\" is not valid");
		}

		this.id = id;
	}

	public C getContainer(){
		return this.container;
	}

	private void setContainer(C container){
		this.container = container;
	}

	public State getState(){
		return this.state;
	}

	void setState(State state){
		this.state = state;
	}

	public Payload getPayload(){
		return this.payload;
	}

	public void setPayload(Payload payload){

		try {
			removePayload();
		} catch(IOException ioe){
			// Ignored
		}

		if(getState().equals(State.NORMAL)){
			setState(State.MODIFIED);
		}

		this.payload = payload;
	}

	private void removePayload() throws IOException {

		if(this.payload instanceof Closeable){
			Closeable closeable = (Closeable)this.payload;

			closeable.close();
		}

		this.payload = null;
	}

	public Qdb getQdb(){
		C container = getContainer();

		return container.getQdb();
	}

	/**
	 * Binary payloads should be manipulated as byte arrays.
	 * Non-binary payloads (in other words, text payloads) should be manipulated as strings.
	 *
	 * The default implementation makes the decision by reading through the payload and looking for null bytes (0x00).
	 *
	 * @see #loadByteArray()
	 * @see #storeByteArray(byte[])
	 * @see #loadString()
	 * @see #storeString(String)
	 */
	public boolean isBinary() throws IOException {
		InputStream is = getInputStream();

		try {
			byte[] bytes = new byte[3];

			int count = is.read(bytes);

			ByteOrderMask bom = ByteOrderMask.valueOf(bytes, 0, count);
			if(bom != null){
				return false;
			}

			while(true){
				int i = is.read();

				switch(i){
					case -1:
						return false;
					case 0:
						return true;
					default:
						break;
				}
			}
		} finally {
			is.close();
		}
	}

	/**
	 * @see #isBinary()
	 *
	 * @return &quot;application/octet-stream&quot; for binary payloads, &quot;text/plain&quot; for text payloads.
	 */
	public String getMimeType() throws IOException {
		return isBinary() ? "application/octet-stream" : "text/plain";
	}

	public InputStream getInputStream() throws IOException {
		Payload payload = getPayload();

		if(payload != null){
			return payload.getInputStream();
		}

		Qdb qdb = getQdb();

		return (qdb.getStorage()).getInputStream(qdbPath());
	}

	public OutputStream getOutputStream() throws IOException {
		TempFilePayload payload = setTempFilePayload();

		return payload.getOutputStream();
	}

	public byte[] loadByteArray() throws IOException {
		InputStream is = getInputStream();

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				copy(is, os);

				return os.toByteArray();
			} finally {
				os.close();
			}
		} finally {
			is.close();
		}
	}

	public void storeByteArray(byte[] bytes) throws IOException {
		OutputStream os = getOutputStream();

		try {
			os.write(bytes, 0, bytes.length);
		} finally {
			os.close();
		}
	}

	/**
	 * Loads the string in UTF-8 character encoding.
	 *
	 * @see #loadString(String)
	 */
	public String loadString() throws IOException {
		return loadString("UTF-8");
	}

	/**
	 * Loads the string in the user specified character encoding.
	 *
	 * However, when the underlying byte array begins with a known Unicode byte order mark (BOM),
	 * then the BOM specified character encoding takes precedence over the user specified character encoding.
	 *
	 * @param encoding The user specified character encoding.
	 *
	 * @see ByteOrderMask
	 */
	public String loadString(String encoding) throws IOException {
		byte[] bytes = loadByteArray();

		ByteOrderMask bom = ByteOrderMask.valueOf(bytes);
		if(bom != null){
			int offset = (bom.getBytes()).length;

			return new String(bytes, offset, bytes.length - offset, bom.getEncoding());
		}

		return new String(bytes, encoding);
	}

	/**
	 * Stores the string in UTF-8 character encoding.
	 *
	 * @see #storeString(String, String)
	 */
	public void storeString(String string) throws IOException {
		storeString(string, "UTF-8");
	}

	/**
	 * @param encoding The user specified character encoding.
	 */
	public void storeString(String string, String encoding) throws IOException {
		byte[] bytes = string.getBytes(encoding);

		storeByteArray(bytes);
	}

	/**
	 * @throws IllegalStateException If the Cargo is in unknown state.
	 */
	@SuppressWarnings (
		value = {"fallthrough"}
	)
	public void storeChanges() throws IOException {

		if(getState().equals(State.UNKNOWN)){
			throw new IllegalStateException("The Cargo is in unknown state");
		}

		Qdb qdb = getQdb();

		switch(getState()){
			case NORMAL:
				break;
			case ADDED:
				qdb.getStorage().add(qdbPath());
				// Falls through
			case MODIFIED:
				store(this, qdb.getStorage());
				setState(State.NORMAL);
				removePayload();
				break;
			case REMOVED:
				qdb.getStorage().remove(qdbPath());
				setState(State.UNKNOWN);
				removePayload();
				break;
			default:
				break;
		}
	}

	void store(Storage storage) throws IOException {
		store(this, storage);
	}

	protected TempFilePayload setTempFilePayload() throws IOException {
		Qdb qdb = getQdb();

		TempFilePayload payload;

		if(qdb != null){
			payload = new TempFilePayload(qdb.getTempDirectory());
		} else

		{
			payload = new TempFilePayload();
		}

		setPayload(payload);

		return payload;
	}

	protected void close() throws IOException {
		removePayload();
	}

	static
	private void store(Cargo<?> cargo, Storage storage) throws IOException {
		InputStream is = cargo.getInputStream();

		try {
			OutputStream os = storage.getOutputStream(cargo.qdbPath());

			try {
				copy(is, os);
			} finally {
				os.close();
			}
		} finally {
			is.close();
		}
	}

	static
	protected void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];

		while(true){
			int count = is.read(buffer);
			if(count < 0){
				break;
			}

			os.write(buffer, 0, count);
		}

		os.flush();
	}
}