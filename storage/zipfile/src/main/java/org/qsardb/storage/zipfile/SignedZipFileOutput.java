/*
 * Copyright (c) 2009 University of Tartu
 */
package org.qsardb.storage.zipfile;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.*;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.qsardb.model.*;

import org.w3c.dom.*;

public class SignedZipFileOutput extends ZipFileOutput {

	private List<Entry> entries = new ArrayList<Entry>();


	public SignedZipFileOutput(File file) throws IOException {
		super(file);
	}

	public SignedZipFileOutput(OutputStream os){
		super(os);
	}

	@Override
	public OutputStream getOutputStream(final String path) throws IOException {
		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance("SHA");
		} catch(NoSuchAlgorithmException nsae){
			throw new IOException();
		}

		DigestOutputStream os = new DigestOutputStream(super.getOutputStream(path), digest){

			@Override
			public void close() throws IOException {

				try {
					byte[] digest = getMessageDigest().digest();

					SignedZipFileOutput.this.entries.add(new Entry(path, digest));
				} finally {
					super.close();
				}
			}
		};

		return os;
	}

	@Override
	public void close() throws IOException, QdbException {

		try {
			writeSignature();
		} finally {
			super.close();
		}
	}

	private void writeSignature() throws IOException, QdbException {
		Document signature = createSignature();

		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		OutputStream os = super.getOutputStream("META-INF/signature.xml");

		try {
			Transformer transformer = transformerFactory.newTransformer();

			transformer.transform(new DOMSource(signature), new StreamResult(os));
		} catch(Exception e){
			throw new QdbException(e);
		} finally {
			os.close();
		}
	}

	private Document createSignature() throws QdbException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);

		try {
			XMLSignatureFactory signatureFactory = XMLSignatureFactoryUtil.getDOMInstance();

			List<Reference> references = new ArrayList<Reference>();
			for(Entry entry : this.entries){
				references.add(signatureFactory.newReference(entry.getPath(), signatureFactory.newDigestMethod(DigestMethod.SHA1, null), Collections.emptyList(), null, null, entry.getDigest()));
			}

			SignedInfo signedInfo = signatureFactory.newSignedInfo(signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec)null), signatureFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null), references);

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(512);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();

			KeyValue keyValue = keyInfoFactory.newKeyValue(keyPair.getPublic());
			KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));

			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			DOMSignContext signContext = new DOMSignContext(keyPair.getPrivate(), document);

			XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
			signature.sign(signContext);

			return document;
		} catch(Exception e){
			throw new QdbException(e);
		}
	}

	static
	private class Entry {

		private String path = null;

		private byte[] digest = null;


		Entry(String path, byte[] digest){
			setPath(path);
			setDigest(digest);
		}

		String getPath(){
			return this.path;
		}

		private void setPath(String path){
			this.path = path;
		}

		byte[] getDigest(){
			return this.digest;
		}

		private void setDigest(byte[] digest){
			this.digest = digest;
		}
	}
}